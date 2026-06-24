export interface KpiSummary {
  totalPatients: number;
  totalDoctors: number;
  monthlyRevenue: number;
  pendingBills: number;
  patientTrend: number;
  doctorTrend: number;
  revenueTrend: number;
  pendingTrend: number;
}

export interface MonthlyCount {
  monthKey: string;
  count: number;
}

export interface CategoryCount {
  labelKey: string;
  count: number;
}

export interface RevenueExpense {
  monthKey: string;
  revenue: number;
  expenses: number;
}

export interface PaymentStatus {
  statusKey: string;
  amount: number;
}

export interface RecentTransaction {
  date: string;
  patientName: string;
  descriptionKey: string;
  amount: number;
  statusKey: string;
}

export interface ReportsDashboard {
  kpis: KpiSummary;
  patientRegistrations: MonthlyCount[];
  patientsByGender: CategoryCount[];
  doctorsBySpeciality: CategoryCount[];
  appointmentsByDoctor: CategoryCount[];
  revenueExpenses: RevenueExpense[];
  paymentStatus: PaymentStatus[];
  recentTransactions: RecentTransaction[];
}
