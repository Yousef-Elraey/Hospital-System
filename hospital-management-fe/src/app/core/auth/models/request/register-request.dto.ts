export interface RegisterRequest {
  userName: string;
  phone: string;
  email: string;
  password: string;
  fullName: string;
  address: string;
  // Not filled by the user on the signup form - see defaults in AuthService.register().
  // TODO: confirm these match the exact values/enum names your backend expects.
  summary: string;
  status: string;
  role: string;
  active: boolean;
}