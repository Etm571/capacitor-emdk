import { registerPlugin } from '@capacitor/core';

import type { EMDKPlugin } from './definitions';

const EMDK = registerPlugin<EMDKPlugin>('EMDK', {
  web: () => import('./web').then((m) => new m.EMDKWeb()),
});

export * from './definitions';
export { EMDK };
