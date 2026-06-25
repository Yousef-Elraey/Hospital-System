export interface ParsedMedication {
  name: string;
  dose: string;
  frequency: string;
  duration: string;
}

export interface ParsedTreatment {
  medications: ParsedMedication[];
  clinicalNotes: string;
  visitType: string | null;
}

export function parseDiagnoses(diagnose: string | undefined | null): string[] {
  if (!diagnose?.trim()) return [];
  return diagnose
    .split(';')
    .map((s) => s.trim())
    .filter(Boolean);
}

export function mainDiagnosis(diagnose: string | undefined | null): string {
  const items = parseDiagnoses(diagnose);
  return items[0] ?? '-';
}

export function parseTreatment(treatment: string | undefined | null): ParsedTreatment {
  const result: ParsedTreatment = { medications: [], clinicalNotes: '', visitType: null };
  if (!treatment?.trim() || treatment.trim() === '-') return result;

  const lines = treatment.split('\n').map((l) => l.trim());
  let section: 'none' | 'medications' | 'notes' = 'none';
  const noteLines: string[] = [];

  for (const line of lines) {
    if (line === 'Medications:') {
      section = 'medications';
      continue;
    }
    if (line === 'Clinical notes:') {
      section = 'notes';
      continue;
    }
    if (line.startsWith('Visit type:')) {
      result.visitType = line.replace('Visit type:', '').trim();
      continue;
    }
    if (section === 'medications' && line.startsWith('- ')) {
      const body = line.slice(2);
      const [name, dose, frequency, durationPart] = body.split('|').map((p) => p.trim());
      const duration = durationPart?.replace(/\s*days?$/i, '') ?? '';
      result.medications.push({
        name: name ?? body,
        dose: dose ?? '',
        frequency: frequency ?? '',
        duration,
      });
      continue;
    }
    if (section === 'notes' && line) {
      noteLines.push(line);
    }
  }

  if (!result.medications.length && !noteLines.length && !result.visitType) {
    result.clinicalNotes = treatment.trim();
  } else {
    result.clinicalNotes = noteLines.join('\n');
  }

  return result;
}

export function visitNumber(recordId: number, createdAt?: string): string {
  const year = createdAt ? new Date(createdAt).getFullYear() : new Date().getFullYear();
  return `V-${year}-${recordId}`;
}

export function formatMedicationLine(med: ParsedMedication): string {
  const parts = [med.name];
  if (med.dose) parts.push(med.dose);
  if (med.frequency) parts.push(med.frequency);
  if (med.duration) parts.push(`${med.duration} days`);
  return parts.join(' — ');
}

export interface SelectedDiagnosis {
  code: string;
  label: string;
}

export function diagnosesToSelected(diagnose: string | undefined | null): SelectedDiagnosis[] {
  return parseDiagnoses(diagnose).map((label) => {
    const codeMatch = label.match(/\(([^)]+)\)\s*$/);
    const code = codeMatch ? codeMatch[1].trim() : label;
    return { code, label };
  });
}

export function buildTreatmentText(
  medications: ParsedMedication[],
  clinicalNotes: string,
  visitType: string,
): string {
  const lines: string[] = [];
  if (medications.length) {
    lines.push('Medications:');
    for (const m of medications) {
      lines.push(`- ${m.name} | ${m.dose} | ${m.frequency} | ${m.duration} days`);
    }
  }
  if (clinicalNotes.trim()) {
    if (lines.length) lines.push('');
    lines.push('Clinical notes:');
    lines.push(clinicalNotes.trim());
  }
  if (visitType !== 'outpatient') {
    lines.push(`Visit type: ${visitType}`);
  }
  return lines.join('\n') || '-';
}
