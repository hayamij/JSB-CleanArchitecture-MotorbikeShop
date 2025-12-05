package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData;
import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData.AccessoryItem;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.GetAllAccessoriesInputBoundary;
import com.motorbike.business.usecase.output.GetAllAccessoriesOutputBoundary;
<<<<<<< HEAD
=======
import com.motorbike.domain.entities.SanPham;
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
import com.motorbike.domain.entities.PhuKienXeMay;

import java.util.List;
import java.util.stream.Collectors;

<<<<<<< HEAD
public class GetAllAccessoriesUseCaseControl implements GetAllAccessoriesInputBoundary {

    private final GetAllAccessoriesOutputBoundary outputBoundary;
    private final ProductRepository productRepository;

    public GetAllAccessoriesUseCaseControl(
            GetAllAccessoriesOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
=======
public class GetAllAccessoriesUseCaseControl
    extends AbstractUseCaseControl<Void, GetAllAccessoriesOutputBoundary> implements GetAllAccessoriesInputBoundary {

    private final ProductRepository productRepository;

    public GetAllAccessoriesUseCaseControl(GetAllAccessoriesOutputBoundary outputBoundary,
                                          ProductRepository productRepository) {
        super(outputBoundary);
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
        this.productRepository = productRepository;
    }

    @Override
<<<<<<< HEAD
    public void execute() {
        GetAllAccessoriesOutputData outputData = null;
        Exception errorException = null;

        try {
            List<PhuKienXeMay> all = productRepository.findAllAccessories();

            List<AccessoryItem> items = all.stream()
                    .map(pk -> new AccessoryItem(
                            pk.getMaSanPham(),
                            pk.getTenSanPham(),
                            pk.getMoTa(),
                            pk.getGia(),
                            pk.getSoLuongTonKho(),
                            pk.getHinhAnh(),
                            pk.getLoaiPhuKien(),
                            pk.getThuongHieu(),
                            pk.getChatLieu(),
                            pk.getKichThuoc()
                    ))
                    .collect(Collectors.toList());

            outputData = new GetAllAccessoriesOutputData(items);
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new GetAllAccessoriesOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
=======
    protected void validateInput(Void inputData) {
        // no input
    }

    @Override
    protected void executeBusinessLogic(Void inputData) {
        try {
            List<SanPham> allProducts = productRepository.findAll();

            List<AccessoryItem> accessories = allProducts.stream()
                    .filter(p -> p instanceof PhuKienXeMay)
                    .map(p -> mapToItem((PhuKienXeMay) p))
                    .collect(Collectors.toList());

            GetAllAccessoriesOutputData outputData = new GetAllAccessoriesOutputData(accessories);
            outputBoundary.present(outputData);

        } catch (Exception e) {
            GetAllAccessoriesOutputData error = new GetAllAccessoriesOutputData("SYSTEM_ERROR", e.getMessage());
            outputBoundary.present(error);
        }
    }

    private AccessoryItem mapToItem(PhuKienXeMay p) {
        return new AccessoryItem(
                p.getMaSanPham(),
                p.getTenSanPham(),
                p.getMoTa(),
                p.getGia(),
                p.getSoLuongTonKho(),
                p.getHinhAnh(),
                p.getLoaiPhuKien(),
                p.getThuongHieu(),
                p.getChatLieu(),
                p.getKichThuoc()
        );
    }

    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        GetAllAccessoriesOutputData error = new GetAllAccessoriesOutputData("INVALID_INPUT", e.getMessage());
        outputBoundary.present(error);
    }

    @Override
    protected void handleSystemError(Exception e) {
        GetAllAccessoriesOutputData error = new GetAllAccessoriesOutputData("SYSTEM_ERROR", e.getMessage());
        outputBoundary.present(error);
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
    }
}
