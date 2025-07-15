# Capacitor EMDK

![capacitor-version](https://img.shields.io/badge/Capacitor-v6--v7-green)
[![npm version](https://img.shields.io/npm/v/capacitor-emdk.svg)](https://www.npmjs.com/package/capacitor-emdk)
[![npm downloads](https://img.shields.io/npm/dm/capacitor-emdk.svg)](https://www.npmjs.com/package/capacitor-emdk)
[![license](https://img.shields.io/npm/l/capacitor-emdk.svg)](https://github.com/etm571/capacitor-emdk/blob/main/LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/etm571/capacitor-emdk/pulls)
[![Issues](https://img.shields.io/github/issues/etm571/capacitor-emdk.svg)](https://github.com/etm571/capacitor-emdk/issues)

Bridges (some) Zebra's EMDK features to Capacitor

> **Disclaimer:**  
> This is a community project and is **not affiliated with Zebra Technologies**.  
> Due to legal reasons, you must manually download the EMDK SDK from [Zebra's offical mirror](https://verve.jfrog.io/artifactory/verve-gradle-release/com/symbol/emdk/9.1.1/emdk-9.1.1.jar), rename the file to `emdk.jar`, and copy it to `node_modules/capacitor-emdk/android/libs`.

## Install

```bash
npm install capacitor-emdk
npx cap sync
```

## Usage

All API methods are **async** and return Promises.

```typescript
import { EMDK } from 'capacitor-emdk';

// Unlock the cradle
async function unlockCradle() {
  try {
    await EMDK.unlockCradle();
    alert("Cradle unlocked successfully.");
  } catch (error) {
    alert("Failed to unlock cradle: " + error);
  }
}

// Get cradle info
async function getCradleInfo() {
  try {
    const info = await EMDK.cradleInfo();
    console.log("Cradle info:", info);
  } catch (error) {
    console.error("Failed to get cradle info:", error);
  }
}
```

## API

<docgen-index>

* [`unlockCradle()`](#unlockcradle)
* [`cradleInfo()`](#cradleinfo)

</docgen-index>

<docgen-api>

### unlockCradle()

```typescript
unlockCradle() => Promise<void>
```

Unlocks the cradle

--------------------


### cradleInfo()

```typescript
cradleInfo() => Promise<void>
```

Receives cradle info

--------------------

</docgen-api>
