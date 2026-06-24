import { Component, OnDestroy, OnInit } from '@angular/core';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Chart, ChartConfiguration, ChartData, registerables } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';
import { Subject, takeUntil } from 'rxjs';
import { PageHeaderComponent } from '../../core/components/page-header/page-header.component';
import { IconComponent } from '../../core/components/icon/icon.component';
import { CHART_COLORS } from '../../core/theme/module-colors';
import { ReportsService } from './services/reports.service';
import type { ReportsDashboard } from './models/reports-dashboard.dto';

Chart.register(...registerables);

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [
    TranslateModule,
    PageHeaderComponent,
    IconComponent,
    NgChartsModule,
    CurrencyPipe,
    DecimalPipe,
  ],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css'],
})
export class ReportsComponent implements OnInit, OnDestroy {
  loading = true;
  dashboard: ReportsDashboard | null = null;
  private destroy$ = new Subject<void>();

  patientRegChart: ChartData<'bar'> = { labels: [], datasets: [] };
  genderChart: ChartData<'doughnut'> = { labels: [], datasets: [] };
  specialityChart: ChartData<'bar'> = { labels: [], datasets: [] };
  doctorApptChart: ChartData<'bar'> = { labels: [], datasets: [] };
  revenueChart: ChartData<'line'> = { labels: [], datasets: [] };
  paymentChart: ChartData<'doughnut'> = { labels: [], datasets: [] };

  barOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: { legend: { display: false } },
    scales: { y: { beginAtZero: true } },
  };

  horizontalBarOptions: ChartConfiguration<'bar'>['options'] = {
    indexAxis: 'y',
    responsive: true,
    maintainAspectRatio: false,
    plugins: { legend: { display: false } },
    scales: { x: { beginAtZero: true } },
  };

  lineOptions: ChartConfiguration<'line'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: { legend: { position: 'bottom' } },
    scales: { y: { beginAtZero: true } },
  };

  doughnutOptions: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: { legend: { position: 'bottom' } },
  };

  constructor(
    private reportsService: ReportsService,
    private translate: TranslateService,
  ) {}

  ngOnInit(): void {
    this.reportsService.getDashboard().subscribe((data) => {
      this.dashboard = data;
      this.loading = false;
      this.buildCharts();
    });

    this.translate.onLangChange.pipe(takeUntil(this.destroy$)).subscribe(() => this.buildCharts());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  trendClass(value: number): string {
    if (value > 0) return 'text-success';
    if (value < 0) return 'text-danger';
    return 'text-muted';
  }

  statusBadgeClass(statusKey: string): string {
    if (statusKey.includes('paid')) return 'bg-success';
    if (statusKey.includes('pending')) return 'bg-warning text-dark';
    if (statusKey.includes('overdue')) return 'bg-danger';
    return 'bg-info text-dark';
  }

  private t(key: string): string {
    return this.translate.instant(key);
  }

  private buildCharts(): void {
    if (!this.dashboard) return;

    const d = this.dashboard;

    this.patientRegChart = {
      labels: d.patientRegistrations.map((x) => this.t(x.monthKey)),
      datasets: [{
        data: d.patientRegistrations.map((x) => x.count),
        backgroundColor: CHART_COLORS.primary,
        borderRadius: 6,
      }],
    };

    this.genderChart = {
      labels: d.patientsByGender.map((x) => this.t(x.labelKey)),
      datasets: [{
        data: d.patientsByGender.map((x) => x.count),
        backgroundColor: CHART_COLORS.doughnut,
      }],
    };

    this.specialityChart = {
      labels: d.doctorsBySpeciality.map((x) => this.t(x.labelKey)),
      datasets: [{
        data: d.doctorsBySpeciality.map((x) => x.count),
        backgroundColor: CHART_COLORS.secondary,
        borderRadius: 6,
      }],
    };

    this.doctorApptChart = {
      labels: d.appointmentsByDoctor.map((x) => this.t(x.labelKey)),
      datasets: [{
        data: d.appointmentsByDoctor.map((x) => x.count),
        backgroundColor: CHART_COLORS.info,
        borderRadius: 6,
      }],
    };

    this.revenueChart = {
      labels: d.revenueExpenses.map((x) => this.t(x.monthKey)),
      datasets: [
        {
          label: this.t('reports.charts.revenue'),
          data: d.revenueExpenses.map((x) => x.revenue),
          borderColor: CHART_COLORS.success,
          backgroundColor: 'rgba(5, 150, 105, 0.1)',
          fill: true,
          tension: 0.3,
        },
        {
          label: this.t('reports.charts.expenses'),
          data: d.revenueExpenses.map((x) => x.expenses),
          borderColor: CHART_COLORS.danger,
          backgroundColor: 'rgba(220, 38, 38, 0.1)',
          fill: true,
          tension: 0.3,
        },
      ],
    };

    this.paymentChart = {
      labels: d.paymentStatus.map((x) => this.t(x.statusKey)),
      datasets: [{
        data: d.paymentStatus.map((x) => x.amount),
        backgroundColor: [...CHART_COLORS.payment],
      }],
    };
  }
}
