package com.motorbike.business.usecase.control;

public abstract class AbstractUseCaseControl<I, O> {

    protected final O outputBoundary;

    public AbstractUseCaseControl(O outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    public final void execute(I inputData) {
        try {
            validateInput(inputData);         
            executeBusinessLogic(inputData);
        } catch (IllegalArgumentException e) {
            handleValidationError(e);
        } catch (Exception e) {
            handleSystemError(e);
        }
    }

    protected abstract void validateInput(I inputData);
    protected abstract void executeBusinessLogic(I inputData) throws Exception;
    protected abstract void handleValidationError(IllegalArgumentException e);
    protected abstract void handleSystemError(Exception e);
    protected void checkInputNotNull(I inputData) {
        if (inputData == null) {
            throw new IllegalArgumentException("Input data không được null");
        }
    }
}
