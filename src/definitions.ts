export interface EMDKPlugin {
  unlockCradle(): Promise<{ status: string }>;
  getCradleInfo(): Promise<{
    firmwareVersion: string;
    dateOfManufacture: string;
    hardwareID: string;
    partNumber: string;
    serialNumber: string;
  }>;
}
