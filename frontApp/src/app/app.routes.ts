import { PreloadAllModules, RouterModule, Routes } from '@angular/router'
import { NgModule } from '@angular/core'
import {roleGuard} from "./guards/role.guard";

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'products',
    pathMatch: 'full',
  },
  {
    path: 'me',
    loadComponent: () => import('./pages/me/me.page').then((m) => m.MePage),
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
    loadComponent: () =>
      import('./pages/products/new/new.page').then((m) => m.NewPage),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_WORKER' },
  },
  {
    path: 'restaurants/new',
    loadComponent: () =>
      import('./pages/restaurants/new/new.page').then((m) => m.NewPage),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_ADMIN' },
  },
  {
    path: 'categories',
    loadComponent: () =>
      import('./pages/categories/categories.page').then(
        (m) => m.CategoriesPage,
      ),
  },
  {
    path: 'categories/new',
    loadComponent: () =>
      import('./pages/categories/new/new.page').then((m) => m.NewPage),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_ADMIN' },
  },
  {
    path: 'evaluations',
    loadComponent: () =>
      import('./pages/evaluations/evaluations.page').then(
        (m) => m.EvaluationsPage,
      ),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_ADMIN' },
  },
  {
    path: 'evaluations/new',
    loadComponent: () =>
      import('./pages/evaluations/new/new.page').then((m) => m.NewPage),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_USER' },
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./pages/register/register.page').then((m) => m.RegisterPage),
  },
  {
    path: 'categories/:id',
    loadComponent: () =>
      import('./pages/categories/update/update.page').then((m) => m.UpdatePage),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_ADMIN' },
  },
  {
    path: 'evaluations/:id',
    loadComponent: () =>
      import('./pages/evaluations/update/update.page').then(
        (m) => m.UpdatePage,
      ),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_USER' },
  },
  {
    path: 'restaurants/:id',
    loadComponent: () =>
      import('./pages/restaurants/update/update.page').then(
        (m) => m.UpdatePage,
      ),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_ADMIN' },
  },
  {
    path: 'products/:id',
    loadComponent: () =>
      import('./pages/products/update/update.page').then((m) => m.UpdatePage),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_WORKER' },
  },
  {
    path: 'products/patchImage/:id',
    loadComponent: () =>
      import('./pages/products/update-image/update-image.page').then(
        (m) => m.UpdateImagePage,
      ),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_WORKER' },
  },
  {
    path: 'orders',
    loadComponent: () =>
      import('./pages/orders/orders.page').then((m) => m.OrdersPage),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_WORKER' },
  },
  {
    path: 'offers',
    loadComponent: () =>
      import('./pages/offers/offers.page').then((m) => m.OffersPage),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_WORKER' },
  },
  {
    path: 'offers/new',
    loadComponent: () =>
      import('./pages/offers/new/new.page').then((m) => m.NewPage),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_ADMIN' },
  },
  {
    path: 'offers/:id',
    loadComponent: () =>
      import('./pages/offers/update/update.page').then((m) => m.UpdatePage),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_ADMIN' },
  },
  {
    path: 'websocket-orders',
    loadComponent: () =>
      import('./pages/websocket-orders/websocket-orders.page').then(
        (m) => m.WebsocketOrdersPage,
      ),
    canActivate: [roleGuard],
    data: { expectedRole: 'ROLE_WORKER' },
  },
  {
    path: 'cart',
    loadComponent: () => import('./pages/cart/cart.page').then( m => m.CartPage)
  },
]

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
