/**
 * Gender options for patient forms.
 * Values are sent to the API as-is.
 */
export enum Gender {
  Male = 'MALE',
  Female = 'FEMALE',
  Other = 'OTHER',
}

export const GENDER_OPTIONS: Gender[] = [Gender.Male, Gender.Female];
