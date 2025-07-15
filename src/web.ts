import { WebPlugin } from '@capacitor/core';

import type { EMDKPlugin } from './definitions';

export class EMDKWeb extends WebPlugin implements EMDKPlugin {
  async unlockCradle(): Promise<void> {
    throw 'EMDK is not supported on web';
  }
  async cradleInfo(): Promise<void> {
    throw 'EMDK is not supported on web';
  }
}
