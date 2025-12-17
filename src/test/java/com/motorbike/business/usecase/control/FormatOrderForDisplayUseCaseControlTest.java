package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatorderfordisplay.FormatOrderForDisplayInputData;
import com.motorbike.business.dto.formatorderfordisplay.FormatOrderForDisplayOutputData;
import com.motorbike.business.usecase.output.FormatOrderForDisplayOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.ChiTietDonHang;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormatOrderForDisplayUseCaseControlTest {

    @Mock
    private FormatOrderForDisplayOutputBoundary outputBoundary;

    private FormatOrderForDisplayUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new FormatOrderForDisplayUseCaseControl(outputBoundary);
    }

    @Test
    void shouldFormatOrderSuccessfully() {
        // Given
        DonHang order = new DonHang(1L, 90000000.0, "PENDING");
        order.setMaDH(1L);

        ChiTietDonHang detail1 = new ChiTietDonHang(1L, 1L, 2, 45000000.0);
        ChiTietDonHang detail2 = new ChiTietDonHang(1L, 2L, 3, 500000.0);

        List<ChiTietDonHang> orderDetails = Arrays.asList(detail1, detail2);

        FormatOrderForDisplayInputData inputData = new FormatOrderForDisplayInputData(order, orderDetails);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(FormatOrderForDisplayOutputData.class));
    }
}
