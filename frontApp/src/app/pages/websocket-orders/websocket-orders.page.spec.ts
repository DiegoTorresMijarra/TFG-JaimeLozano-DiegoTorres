import { ComponentFixture, TestBed } from '@angular/core/testing';
import { WebsocketOrdersPage } from './websocket-orders.page';

describe('WebsocketOrdersPage', () => {
  let component: WebsocketOrdersPage;
  let fixture: ComponentFixture<WebsocketOrdersPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(WebsocketOrdersPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
