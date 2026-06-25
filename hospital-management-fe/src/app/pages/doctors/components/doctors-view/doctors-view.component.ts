import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { DoctorService } from '../../services/doctor.service';
import type { DoctorResponse } from '../../models/response/doctor-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-doctors-view',
  standalone: true,
  imports: [TranslateModule, RouterLink, PageHeaderComponent],
  templateUrl: './doctors-view.component.html',
  styleUrls: ['./doctors-view.component.css'],
})
export class DoctorsViewComponent implements OnInit {
  id: number | null = null;
  doctor: DoctorResponse | null = null;
  loading = false;

  constructor(private doctorService: DoctorService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.doctorService.getDoctor(this.id).subscribe({
      next: (data) => { this.doctor = data; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }
}
