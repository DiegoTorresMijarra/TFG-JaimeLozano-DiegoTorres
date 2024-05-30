import { PreloadAllModules, RouterModule, Routes } from '@angular/router'
import { NgModule } from '@angular/core'

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'products',
    pathMatch: 'full',
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./pages/login/login.page').then((m) => m.LoginPage),
  },
  {
    path: 'products',
    loadComponent: () =>
      import('./pages/products/products.page').then((m) => m.ProductsPage),
  },
  {
    path: 'restaurants',
    loadComponent: () =>
      import('./pages/restaurants/restaurants.page').then(
        (m) => m.RestaurantsPage,
      ),
  },
  {
    path: 'products/new',
    loadComponent: () => import('./pages/products/new/new.page').then( m => m.NewPage)
  },
  {
    path: 'restaurants/new',
    loadComponent: () => import('./pages/restaurants/new/new.page').then( m => m.NewPage)
  },
  {
    path: 'categories',
    loadComponent: () => import('./pages/categories/categories.page').then(m => m.CategoriesPage)
  },
  {
    path: 'categories/new',
    loadComponent: () => import('./pages/categories/new/new.page').then( m => m.NewPage)
  },



]

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
