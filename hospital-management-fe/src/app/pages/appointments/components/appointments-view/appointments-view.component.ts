import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { AppointmentService } from '../../services/appointment.service';
import { LocaleService } from '../../../../core/services/locale.service';
import { formatDateTimeDisplay } from '../../utils/date-form';
import type { AppointmentResponse } from '../../models/response/appointment-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-appointments-view',
  standalone: true,
  imports: [TranslateModule, RouterLink, PageHeaderComponent],
  templateUrl: './appointments-view.component.html',
  styleUrls: ['./appointments-view.component.css'],
})
export class AppointmentsViewComponent implements OnInit {
  id: number | null = null;
  appointment: AppointmentResponse | null = null;
  loading = false;

  constructor(
    private appointmentService: AppointmentService,
    private route: ActivatedRoute,
    public locale: LocaleService,
  ) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.appointmentService.getAppointment(this.id).subscribe({
      next: (data) => { this.appointment = data; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }

  formatTiming(t: string | undefined): string {
    return formatDateTimeDisplay(t, this.locale.currentLang);
  }
}
