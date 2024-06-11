import { enableProdMode } from '@angular/core'
import { setAssetPath } from '@stencil/core'
import { environment } from './environments/environment'
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic'
import { AppModule } from './app/app.module'

if (environment.production) {
  enableProdMode()
}

setAssetPath(`${window.location.origin}/`)

platformBrowserDynamic()
  .bootstrapModule(AppModule)
  .catch((err) => console.log(err))
