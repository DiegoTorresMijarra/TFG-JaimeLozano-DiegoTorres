import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UpdateImagePage } from './update-image.page';

describe('UpdateImagePage', () => {
  let component: UpdateImagePage;
  let fixture: ComponentFixture<UpdateImagePage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateImagePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
