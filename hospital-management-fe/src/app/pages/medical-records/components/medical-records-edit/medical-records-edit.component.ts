import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { MedicalRecordService } from '../../services/medical-record.service';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { UpdateMedicalRecordRequest } from '../../models/request/update-medical-record-request.dto';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { LocaleService } from '../../../../core/services/locale.service';
import { formatDateDisplay } from '../../../../core/utils/display-date';
import {
  buildTreatmentText,
  diagnosesToSelected,
  parseTreatment,
  type SelectedDiagnosis,
} from '../../utils/medical-record-display';

interface DiagnosisOption {
  code: string;
  nameEn: string;
  nameAr: string;
}

interface MedicationRow {
  uid: number;
  name: string;
  dose: string;
  frequency: string;
  duration: string;
}

interface MedicationOption {
  nameEn: string;
  nameAr: string;
}

interface QuickTemplate {
  id: string;
  labelKey: string;
  diagnoses: string[];
  medications: Omit<MedicationRow, 'uid'>[];
}

const DIAGNOSIS_CATALOG: DiagnosisOption[] = [
  { code: 'I10', nameEn: 'Hypertension', nameAr: 'ارتفاع ضغط الدم' },
  { code: 'E11', nameEn: 'Type 2 Diabetes', nameAr: 'السكري النوع الثاني' },
  { code: 'J06.9', nameEn: 'Upper respiratory infection', nameAr: 'عدوى الجهاز التنفسي العلوي' },
  { code: 'M54.5', nameEn: 'Low back pain', nameAr: 'ألم أسفل الظهر' },
  { code: 'K21.0', nameEn: 'GERD', nameAr: 'ارتجاع المريء' },
  { code: 'J45.9', nameEn: 'Asthma', nameAr: 'الربو' },
  { code: 'E78.5', nameEn: 'Hyperlipidemia', nameAr: 'فرط شحميات الدم' },
  { code: 'N39.0', nameEn: 'UTI', nameAr: 'التهاب المسالك البولية' },
];

const MEDICATION_CATALOG: MedicationOption[] = [
  { nameEn: 'Metformin 500mg', nameAr: 'ميتفورمين 500 مجم' },
  { nameEn: 'Amlodipine 5mg', nameAr: 'أملوديبين 5 مجم' },
  { nameEn: 'Atorvastatin 20mg', nameAr: 'أتورفاستاتين 20 مجم' },
  { nameEn: 'Omeprazole 20mg', nameAr: 'أوميبرازول 20 مجم' },
  { nameEn: 'Paracetamol 500mg', nameAr: 'باراسيتامول 500 مجم' },
  { nameEn: 'Amoxicillin 500mg', nameAr: 'أموكسيسيلين 500 مجم' },
  { nameEn: 'Salbutamol inhaler', nameAr: 'بخاخ سالبيوتامول' },
  { nameEn: 'Losartan 50mg', nameAr: 'لوسارتان 50 مجم' },
];

@Component({
  selector: 'app-medical-records-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, PageHeaderComponent, NgSelectModule],
  templateUrl: './medical-records-edit.component.html',
  styleUrls: ['./medical-records-edit.component.css'],
})
export class MedicalRecordsEditComponent implements OnInit {
  id: number | null = null;
  loading = false;
  doctors: DoctorResponse[] = [];
  patients: PatientResponse[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  doctorSelectOptions: { value: number; label: string }[] = [];

  model: { patientId: number | null; doctorId: number | null } = {
    patientId: null,
    doctorId: null,
  };

  visitType = 'outpatient';
  visitDateIso = '';
  clinicalNotes = '';
  diagnosisSearch = '';
  medicationSearch = '';
  selectedDiagnoses: SelectedDiagnosis[] = [];
  medications: MedicationRow[] = [];
  showMoreDiagnoses = false;
  submitted = false;
  private medicationUid = 0;

  readonly visitTypes = [
    { value: 'outpatient', labelKey: 'medicalRecords.visitTypeOutpatient' },
    { value: 'inpatient', labelKey: 'medicalRecords.visitTypeInpatient' },
    { value: 'emergency', labelKey: 'medicalRecords.visitTypeEmergency' },
  ];

  readonly doseOptions = ['250 mg', '500 mg', '5 mg', '10 mg', '20 mg', '50 mg'];
  readonly frequencyOptions = [
    { value: 'OD', labelKey: 'medicalRecords.freqOd' },
    { value: 'BID', labelKey: 'medicalRecords.freqBid' },
    { value: 'TID', labelKey: 'medicalRecords.freqTid' },
    { value: 'QID', labelKey: 'medicalRecords.freqQid' },
  ];
  readonly durationOptions = [
    { value: '7', labelKey: 'medicalRecords.duration7' },
    { value: '14', labelKey: 'medicalRecords.duration14' },
    { value: '30', labelKey: 'medicalRecords.duration30' },
    { value: '90', labelKey: 'medicalRecords.duration90' },
  ];

  readonly commonDiagnosisCodes = ['I10', 'E11', 'J06.9', 'M54.5', 'K21.0'];
  readonly quickTemplates: QuickTemplate[] = [
    {
      id: 'hypertension',
      labelKey: 'medicalRecords.templateHypertension',
      diagnoses: ['I10'],
      medications: [{ name: 'Amlodipine 5mg', dose: '5 mg', frequency: 'OD', duration: '30' }],
    },
    {
      id: 'diabetes',
      labelKey: 'medicalRecords.templateDiabetes',
      diagnoses: ['E11'],
      medications: [{ name: 'Metformin 500mg', dose: '500 mg', frequency: 'BID', duration: '30' }],
    },
  ];

  constructor(
    private medicalRecordService: MedicalRecordService,
    private doctorService: DoctorService,
    private patientService: PatientService,
    private router: Router,
    private route: ActivatedRoute,
    public locale: LocaleService,
  ) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;

    this.doctorService.getDoctors().subscribe((d) => {
      this.doctors = d ?? [];
      this.doctorSelectOptions = this.doctors.map((x) => ({ value: x.id!, label: x.name }));
    });

    this.patientService.getPatients().subscribe((p) => {
      this.patients = p ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });

    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.medicalRecordService.getMedicalRecord(this.id).subscribe({
      next: (data) => {
        this.model = {
          patientId: data.patientId,
          doctorId: data.doctorId,
        };
        this.selectedDiagnoses = diagnosesToSelected(data.diagnose);

        const parsed = parseTreatment(data.treatment);
        this.clinicalNotes = parsed.clinicalNotes;
        this.visitType = parsed.visitType ?? 'outpatient';
        this.medications = parsed.medications.map((m) => ({
          uid: ++this.medicationUid,
          name: m.name,
          dose: m.dose || '500 mg',
          frequency: m.frequency || 'BID',
          duration: m.duration || '30',
        }));

        const dateSource = data.createdAt ?? data.updatedAt;
        if (dateSource) {
          this.visitDateIso = dateSource.slice(0, 10);
        } else {
          const now = new Date();
          this.visitDateIso = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`;
        }

        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  get selectedPatient(): PatientResponse | null {
    if (!this.model.patientId) return null;
    return this.patients.find((p) => p.id === this.model.patientId) ?? null;
  }

  get visitDateDisplay(): string {
    return formatDateDisplay(this.visitDateIso, this.locale.currentLang);
  }

  get visibleCommonDiagnoses(): DiagnosisOption[] {
    const codes = this.showMoreDiagnoses ? DIAGNOSIS_CATALOG.map((d) => d.code) : this.commonDiagnosisCodes;
    return DIAGNOSIS_CATALOG.filter((d) => codes.includes(d.code));
  }

  get filteredDiagnosisResults(): DiagnosisOption[] {
    const q = this.diagnosisSearch.trim().toLowerCase();
    if (!q) return [];
    return DIAGNOSIS_CATALOG.filter((d) => {
      const label = this.diagnosisLabel(d).toLowerCase();
      return label.includes(q) || d.code.toLowerCase().includes(q);
    }).slice(0, 8);
  }

  get filteredMedicationResults(): MedicationOption[] {
    const q = this.medicationSearch.trim().toLowerCase();
    if (!q) return [];
    return MEDICATION_CATALOG.filter((m) => {
      const label = this.medicationLabel(m).toLowerCase();
      return label.includes(q) || m.nameEn.toLowerCase().includes(q);
    }).slice(0, 8);
  }

  diagnosisLabel(d: DiagnosisOption): string {
    const name = this.locale.currentLang === 'ar' ? d.nameAr : d.nameEn;
    return `${name} (${d.code})`;
  }

  medicationLabel(m: MedicationOption | { nameEn: string; nameAr: string }): string {
    return this.locale.currentLang === 'ar' ? m.nameAr : m.nameEn;
  }

  patientAge(dateOfBirth?: string): number | null {
    if (!dateOfBirth) return null;
    const iso = dateOfBirth.slice(0, 10);
    if (!/^\d{4}-\d{2}-\d{2}$/.test(iso)) return null;
    const [y, m, d] = iso.split('-').map(Number);
    const today = new Date();
    let age = today.getFullYear() - y;
    const monthDiff = today.getMonth() + 1 - m;
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < d)) age--;
    return age >= 0 ? age : null;
  }

  genderLabel(gender?: string): string {
    if (!gender) return '-';
    return `patients.gender.${gender.toUpperCase()}`;
  }

  isDiagnosisSelected(code: string): boolean {
    return this.selectedDiagnoses.some((d) => d.code === code);
  }

  addDiagnosis(option: DiagnosisOption): void {
    if (this.isDiagnosisSelected(option.code)) return;
    this.selectedDiagnoses = [
      ...this.selectedDiagnoses,
      { code: option.code, label: this.diagnosisLabel(option) },
    ];
    this.diagnosisSearch = '';
  }

  removeDiagnosis(code: string): void {
    this.selectedDiagnoses = this.selectedDiagnoses.filter((d) => d.code !== code);
  }

  addDiagnosisFromSearch(): void {
    const first = this.filteredDiagnosisResults[0];
    if (first) this.addDiagnosis(first);
  }

  addMedication(option?: MedicationOption, preset?: Omit<MedicationRow, 'uid'>): void {
    const name = preset?.name ?? (option ? this.medicationLabel(option) : this.medicationSearch.trim());
    if (!name) return;
    this.medications = [
      ...this.medications,
      {
        uid: ++this.medicationUid,
        name,
        dose: preset?.dose ?? '500 mg',
        frequency: preset?.frequency ?? 'BID',
        duration: preset?.duration ?? '30',
      },
    ];
    this.medicationSearch = '';
  }

  removeMedication(uid: number): void {
    this.medications = this.medications.filter((m) => m.uid !== uid);
  }

  applyTemplate(template: QuickTemplate): void {
    for (const code of template.diagnoses) {
      const option = DIAGNOSIS_CATALOG.find((d) => d.code === code);
      if (option) this.addDiagnosis(option);
    }
    for (const med of template.medications) {
      this.addMedication(undefined, med);
    }
  }

  toggleMoreDiagnoses(): void {
    this.showMoreDiagnoses = !this.showMoreDiagnoses;
  }

  save(form: NgForm, options: { navigate?: boolean; print?: boolean } = {}): void {
    this.submitted = true;
    if (form.invalid || !this.selectedDiagnoses.length || !this.id) {
      form.control.markAllAsTouched();
      return;
    }
    const request: UpdateMedicalRecordRequest = {
      diagnose: this.selectedDiagnoses.map((d) => d.label).join('; '),
      treatment: buildTreatmentText(this.medications, this.clinicalNotes, this.visitType),
      patientId: this.model.patientId!,
      doctorId: this.model.doctorId!,
    };
    this.medicalRecordService.updateMedicalRecord(this.id, request).subscribe({
      next: () => {
        if (options.print) {
          setTimeout(() => window.print(), 300);
        }
        if (options.navigate !== false) {
          this.router.navigate(['/medical-records', this.id]);
        }
      },
    });
  }

  saveDraft(form: NgForm): void {
    this.save(form, { navigate: false });
  }

  saveAndPrint(form: NgForm): void {
    this.save(form, { print: true });
  }

  resetForm(form: NgForm): void {
    if (this.id) {
      this.load();
      this.submitted = false;
      form.resetForm(this.model);
    }
  }

  cancel(): void {
    this.router.navigate(['/medical-records', this.id]);
  }

  onVoiceInput(): void {
    // Placeholder for future voice-to-text integration
  }
}
