import {Product} from "./product.entity";

export interface Offer {
  id?: number;
  descuento: number;
  fechaDesde: Date;
  fechaHasta: Date;
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
  product: Product
}
export interface OfferDto {
  descuento: number;
  fechaDesde: Date;
  fechaHasta: Date;
  productId: number;
}
