/**
 * Breakpoint values (px). Keep in sync with tokens in src/styles.css.
 */
export const HM_BREAKPOINTS = {
  xs: 400,
  sm: 576,
  md: 768,
  lg: 992,
  appointmentsNarrow: 520,
} as const;

export const HM_DESKTOP_MIN_WIDTH = HM_BREAKPOINTS.lg;
