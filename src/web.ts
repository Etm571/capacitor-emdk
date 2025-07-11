import { WebPlugin } from '@capacitor/core';

import type { EMDKPlugin } from './definitions';

export class EMDKWeb extends WebPlugin implements EMDKPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
