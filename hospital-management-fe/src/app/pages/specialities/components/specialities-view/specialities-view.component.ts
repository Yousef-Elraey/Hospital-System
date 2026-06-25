import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { SpecialityService } from '../../services/speciality.service';
import type { SpecialityResponse } from '../../models/response/speciality-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-specialities-view',
  standalone: true,
  imports: [TranslateModule, RouterLink, PageHeaderComponent],
  templateUrl: './specialities-view.component.html',
  styleUrls: ['./specialities-view.component.css'],
})
export class SpecialitiesViewComponent implements OnInit {
  id: number | null = null;
  speciality: SpecialityResponse | null = null;
  loading = false;

  constructor(private specialityService: SpecialityService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.specialityService.getSpeciality(this.id).subscribe({
      next: (data) => { this.speciality = data; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }
}
