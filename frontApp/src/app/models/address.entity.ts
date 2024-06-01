export interface Address {
  id: string
  country: string
  province: string
  city: string
  street: string
  number: string
  apartment?: string | null
  postalCode: string
  extraInfo?: string | null
  name: string
  createdAt: string
  updatedAt: string
  deletedAt?: string | null
  userId: string
}
