package es.jaimelozanodiegotorres.backapp.rest.offers.service;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.EntityNotFoundException;
import es.jaimelozanodiegotorres.backapp.rest.offers.dto.OfferDto;
import es.jaimelozanodiegotorres.backapp.rest.offers.filters.OfferFilters;
import es.jaimelozanodiegotorres.backapp.rest.offers.mapper.OfferMapper;
import es.jaimelozanodiegotorres.backapp.rest.offers.models.Offer;
import es.jaimelozanodiegotorres.backapp.rest.offers.repository.OfferRepository;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.services.ProductServicePgSqlImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServicePgSqlImpTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private ProductServicePgSqlImp productServiceImp;

    @InjectMocks
    private OfferServicePgSqlImp offerService;


    private final Product product = Product.builder()
            .id(1L)
            .name("Test Product")
            .build();

    private final Offer offer = Offer.builder()
            .id(1L)
            .product(product)
            .descuento(20.0)
            .fechaDesde(LocalDateTime.of(2024, 1, 1, 0, 0))
            .fechaHasta(LocalDateTime.of(2024, 12, 31, 23, 59))
            .build();

    private final OfferDto offerDto = OfferDto.builder()
            .productId(1L)
            .descuento(30.0)
            .fechaDesde(LocalDateTime.of(2024, 1, 1, 0, 0))
            .fechaHasta(LocalDateTime.of(2024, 12, 31, 23, 59))
            .build();


    @Test
    void saveOffer() {
        when(productServiceImp.findById(1L)).thenReturn(product);
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        Offer savedOffer = offerService.save(offerDto);

        verify(offerRepository).save(any(Offer.class));
        assertEquals(offer.getDescuento(), savedOffer.getDescuento());
        assertEquals(product, savedOffer.getProduct());
    }

    @Test
    void updateOffer() {
        when(offerRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(offer));
        Offer updatedOffer = OfferMapper.INSTANCE.updateModel(offer,offerDto);

        when(offerRepository.save(any(Offer.class))).thenReturn(updatedOffer);

        Offer res = offerService.update(1L, offerDto);

        verify(offerRepository).save(any(Offer.class));
        assertEquals(offerDto.getDescuento(), res.getDescuento());
    }

    @Test
    void updateOffer_NotFound() {
        when(offerRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> offerService.update(1L, offerDto));
        verify(offerRepository, never()).save(any(Offer.class));
        verify(offerRepository).findByIdAndDeletedAtIsNull(1L);
    }

    @Test
    void findActivasByProductId() {
        when(offerRepository.findActivasByProduct(1L)).thenReturn(offer);

        Offer foundOffer = offerService.findActivasByProductId(1L);

        assertEquals(offer, foundOffer);
    }
}
