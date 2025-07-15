# Capacitor EMDK

[![npm version](https://img.shields.io/npm/v/capacitor-emdk.svg)](https://www.npmjs.com/package/capacitor-emdk)
[![npm downloads](https://img.shields.io/npm/dm/capacitor-emdk.svg)](https://www.npmjs.com/package/capacitor-emdk)
[![license](https://img.shields.io/npm/l/capacitor-emdk.svg)](https://github.com/etm571/capacitor-emdk/blob/main/LICENSE)

Bridges Zebra's EMDK features to Capacitor

## Install

```bash
npm install capacitor-emdk
npx cap sync
```

## Usage

All API methods are **async** and return Promises.

```typescript
import { EMDK } from 'capacitor-emdk';

// Unlock the cradle asynchronously
async function unlockCradle() {
  try {
    await EMDK.unlockCradle();
    alert("Cradle unlocked successfully.");
  } catch (error) {
    alert("Failed to unlock cradle: " + error);
  }
}

// Get cradle info asynchronously
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
