export interface EMDKPlugin {

  enable(): Promise<void>;
  disable(): Promise<void>;
  unlockCradle(): Promise<void>;
  cradleInfo(): Promise<void>;

}
