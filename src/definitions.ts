export interface EMDKPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
