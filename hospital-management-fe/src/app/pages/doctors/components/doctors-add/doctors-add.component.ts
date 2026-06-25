import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { DoctorService } from '../../services/doctor.service';
import type { CreateDoctorRequest } from '../../models/request/create-doctor-request.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-doctors-add',
  standalone: true,
  imports: [FormsModule, TranslateModule, PageHeaderComponent],
  templateUrl: './doctors-add.component.html',
  styleUrls: ['./doctors-add.component.css'],
})
export class DoctorsAddComponent {
  model: CreateDoctorRequest = { name: '', speciality: '', contactNumber: '' };
  constructor(private doctorService: DoctorService, private router: Router) {}

  save(form: NgForm): void {
    if (form.invalid) return;
    this.doctorService.addDoctor(this.model).subscribe({
      next: () => this.router.navigate(['/doctors']),
    });
  }

  cancel(): void {
    this.router.navigate(['/doctors']);
  }
}
