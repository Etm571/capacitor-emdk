export interface EMDKPlugin {

  unlockCradle(): Promise<void>;
  cradleInfo(): Promise<void>;

}
