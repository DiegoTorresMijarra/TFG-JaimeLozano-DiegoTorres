export class StatusResponseEntity {
  schemaVersion: number;
  label: string;
  message: string;
  color: string;

  constructor(
    schemaVersion: number = 1,
    label: string = 'Status',
    message: string = 'pending',
    color: string = 'orange'
  ) {
    this.schemaVersion = schemaVersion;
    this.label = label;
    this.message = message;
    this.color = color;
  }
}
