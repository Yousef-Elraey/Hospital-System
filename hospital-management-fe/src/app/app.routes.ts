import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent) },
  { path: 'register', loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent) },
  {
    path: '',
    loadComponent: () => import('./core/components/layout/layout.component').then(m => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      { path: '', loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'reports', loadComponent: () => import('./pages/reports/reports.component').then(m => m.ReportsComponent) },
      // Patients
      { path: 'patients', loadComponent: () => import('./pages/patients/components/patients-list/patients-list.component').then(m => m.PatientsListComponent) },
      { path: 'patients/add', loadComponent: () => import('./pages/patients/components/patients-add/patients-add.component').then(m => m.PatientsAddComponent) },
      { path: 'patients/:id', loadComponent: () => import('./pages/patients/components/patients-view/patients-view.component').then(m => m.PatientsViewComponent) },
      { path: 'patients/:id/edit', loadComponent: () => import('./pages/patients/components/patients-edit/patients-edit.component').then(m => m.PatientsEditComponent) },
      // Doctors
      { path: 'doctors', loadComponent: () => import('./pages/doctors/components/doctors-list/doctors-list.component').then(m => m.DoctorsListComponent) },
      { path: 'doctors/add', loadComponent: () => import('./pages/doctors/components/doctors-add/doctors-add.component').then(m => m.DoctorsAddComponent) },
      { path: 'doctors/:id', loadComponent: () => import('./pages/doctors/components/doctors-view/doctors-view.component').then(m => m.DoctorsViewComponent) },
      { path: 'doctors/:id/edit', loadComponent: () => import('./pages/doctors/components/doctors-edit/doctors-edit.component').then(m => m.DoctorsEditComponent) },
      // Specialities
      { path: 'specialities', loadComponent: () => import('./pages/specialities/components/specialities-list/specialities-list.component').then(m => m.SpecialitiesListComponent) },
      { path: 'specialities/add', loadComponent: () => import('./pages/specialities/components/specialities-add/specialities-add.component').then(m => m.SpecialitiesAddComponent) },
      { path: 'specialities/:id', loadComponent: () => import('./pages/specialities/components/specialities-view/specialities-view.component').then(m => m.SpecialitiesViewComponent) },
      { path: 'specialities/:id/edit', loadComponent: () => import('./pages/specialities/components/specialities-edit/specialities-edit.component').then(m => m.SpecialitiesEditComponent) },
      // Appointments
      { path: 'appointments', loadComponent: () => import('./pages/appointments/components/appointments-list/appointments-list.component').then(m => m.AppointmentsListComponent) },
      { path: 'appointments/add', loadComponent: () => import('./pages/appointments/components/appointments-add/appointments-add.component').then(m => m.AppointmentsAddComponent) },
      { path: 'appointments/book', loadComponent: () => import('./pages/appointments/components/appointments-book/appointments-book.component').then(m => m.AppointmentsBookComponent) },
      { path: 'appointment-slots', loadComponent: () => import('./pages/time-slots/components/time-slots-list/time-slots-list.component').then(m => m.TimeSlotsListComponent) },
      { path: 'appointment-slots/add', loadComponent: () => import('./pages/time-slots/components/time-slots-generate/time-slots-generate.component').then(m => m.TimeSlotsGenerateComponent) },
      { path: 'appointments/slots', redirectTo: 'appointment-slots', pathMatch: 'full' },
      { path: 'appointments/:id', loadComponent: () => import('./pages/appointments/components/appointments-view/appointments-view.component').then(m => m.AppointmentsViewComponent) },
      { path: 'appointments/:id/edit', loadComponent: () => import('./pages/appointments/components/appointments-edit/appointments-edit.component').then(m => m.AppointmentsEditComponent) },
      // Billing
      { path: 'billing', loadComponent: () => import('./pages/billing/components/billing-list/billing-list.component').then(m => m.BillingListComponent) },
      { path: 'billing/add', loadComponent: () => import('./pages/billing/components/billing-add/billing-add.component').then(m => m.BillingAddComponent) },
      { path: 'billing/:id', loadComponent: () => import('./pages/billing/components/billing-view/billing-view.component').then(m => m.BillingViewComponent) },
      { path: 'billing/:id/edit', loadComponent: () => import('./pages/billing/components/billing-edit/billing-edit.component').then(m => m.BillingEditComponent) },
      // Medical records
      { path: 'medical-records', loadComponent: () => import('./pages/medical-records/components/medical-records-list/medical-records-list.component').then(m => m.MedicalRecordsListComponent) },
      { path: 'medical-records/add', loadComponent: () => import('./pages/medical-records/components/medical-records-add/medical-records-add.component').then(m => m.MedicalRecordsAddComponent) },
      { path: 'medical-records/:id', loadComponent: () => import('./pages/medical-records/components/medical-records-view/medical-records-view.component').then(m => m.MedicalRecordsViewComponent) },
      { path: 'medical-records/:id/edit', loadComponent: () => import('./pages/medical-records/components/medical-records-edit/medical-records-edit.component').then(m => m.MedicalRecordsEditComponent) },
    ],
  },
  { path: '**', redirectTo: '' },
];
