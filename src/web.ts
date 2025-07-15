import { WebPlugin } from '@capacitor/core';
import type { EMDKPlugin } from './definitions';

export class EMDKWeb extends WebPlugin implements EMDKPlugin {
  async unlockCradle(): Promise<{ status: string }> {
    throw new Error('EMDK is not supported on web');
  }

  async getCradleInfo(): Promise<{
    firmwareVersion: string;
    dateOfManufacture: string;
    hardwareID: string;
    partNumber: string;
    serialNumber: string;
  }> {
    throw new Error('EMDK is not supported on web');
  }
}
