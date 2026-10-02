package com.example.demo;

import com.example.demo.domain.Planta;

public abstract class PlantValidator {

    public final ValidationResult validate(Planta plant) {
        ValidationResult result = new ValidationResult();

        result.setDenumireValid(validateDenumire(plant.getDenumire()));
        result.setSpecieValid(validateSpecie(plant.getSpecie()));
        result.setTipValid(validateTip(plant.getTip()));
        result.setCarnivoraValid(validateCarnivora(plant.isEsteCarnivora()));

        additionalValidation(plant, result);

        result.setValid(result.isDenumireValid() &&
                result.isSpecieValid() &&
                result.isTipValid() &&
                result.isCarnivoraValid());

        return result;
    }

    protected abstract boolean validateDenumire(String denumire);
    protected abstract boolean validateSpecie(String specie);
    protected abstract boolean validateTip(String tip);

    protected boolean validateCarnivora(boolean carnivora) {
        return true;
    }

    protected void additionalValidation(Planta plant, ValidationResult result) {
        //implicit
    }

    public static class ValidationResult {
        private boolean denumireValid;
        private boolean specieValid;
        private boolean tipValid;
        private boolean carnivoraValid;
        private boolean valid;
        private String errorMessage;

        public boolean isDenumireValid() { return denumireValid; }
        public void setDenumireValid(boolean denumireValid) { this.denumireValid = denumireValid; }

        public boolean isSpecieValid() { return specieValid; }
        public void setSpecieValid(boolean specieValid) { this.specieValid = specieValid; }

        public boolean isTipValid() { return tipValid; }
        public void setTipValid(boolean tipValid) { this.tipValid = tipValid; }

        public boolean isCarnivoraValid() { return carnivoraValid; }
        public void setCarnivoraValid(boolean carnivoraValid) { this.carnivoraValid = carnivoraValid; }

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }

        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

        @Override
        public String toString() {
            return String.format("ValidationResult{valid=%s, denumire=%s, specie=%s, tip=%s}",
                    valid, denumireValid, specieValid, tipValid);
        }
    }
}