# Hospital Management – Angular Frontend

Angular 18 frontend for the Hospital Management backend (Spring Boot).

## Requirements

- **Node.js** v18.19+ (or v20.11+ / v22+)
- **npm** 8+

## Setup

```bash
npm install
```

## Development

Start the dev server (default: http://localhost:4200). API calls to `/api/*` are proxied to the backend at http://localhost:8080.

```bash
npm start
```

Ensure the Spring Boot backend is running on port 8080.

## Build

```bash
npm run build
```

Output is in `dist/hospital-management-fe/`.

## Features

- **Dummy login** – Any non-empty username signs you in (no real auth).
- **Dashboard** – Links to all modules.
- **Patients** – List, add, edit, delete.
- **Doctors** – List, add, edit, delete.
- **Appointments** – List, add, edit, delete; “Book” for new patient + appointment.
- **Billing** – List, add, edit, delete (linked to patients).
- **Medical records** – List, add, edit, delete (linked to patients and doctors).

All data is loaded from and saved to the backend API.
