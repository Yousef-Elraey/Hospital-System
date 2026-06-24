import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { DoctorService } from '../../services/doctor.service';
import type { UpdateDoctorRequest } from '../../models/request/update-doctor-request.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-doctors-edit',
  standalone: true,
  imports: [FormsModule, TranslateModule, RouterLink, PageHeaderComponent],
  templateUrl: './doctors-edit.component.html',
  styleUrls: ['./doctors-edit.component.css'],
})
export class DoctorsEditComponent implements OnInit {
  id: number | null = null;
  model: UpdateDoctorRequest = { name: '', speciality: '', contactNumber: '' };
  loading = false;

  constructor(
    private doctorService: DoctorService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.doctorService.getDoctor(this.id).subscribe({
      next: (data) => {
        this.model = { name: data.name, speciality: data.speciality, contactNumber: data.contactNumber };
        this.loading = false;
      },
      error: () => { this.loading = false; },
    });
  }

  save(form: NgForm): void {
    if (form.invalid || !this.id) return;
    this.doctorService.updateDoctor(this.id, this.model).subscribe({
      next: () => this.router.navigate(['/doctors', this.id]),
    });
  }

  cancel(): void {
    this.router.navigate(['/doctors', this.id]);
  }
}
