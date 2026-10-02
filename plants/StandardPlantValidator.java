package com.example.demo;


import com.example.demo.domain.Planta;
import org.springframework.stereotype.Component;

@Component
public class StandardPlantValidator extends PlantValidator {

    @Override
    protected boolean validateDenumire(String denumire) {
        if (denumire == null || denumire.trim().isEmpty()) {
            return false;
        }
        return denumire.length() >= 2 && denumire.length() <= 100;
    }

    @Override
    protected boolean validateSpecie(String specie) {
        if (specie == null || specie.trim().isEmpty()) {
            return false;
        }
        return specie.length() >= 2 && specie.length() <= 50;
    }

    @Override
    protected boolean validateTip(String tip) {
        if (tip == null || tip.trim().isEmpty()) {
            return false;
        }

        return tip.length() >= 2 && tip.length() <= 30;
    }

    @Override
    protected void additionalValidation(Planta plant, ValidationResult result) {
        if (plant.isEsteCarnivora() && plant.getSpecie() != null) {
            String specieLower = plant.getSpecie().toLowerCase();
            if (!specieLower.contains("drosera") &&
                    !specieLower.contains("dionaea") &&
                    !specieLower.contains("sarracenia")) {
                System.out.println("Specia " + plant.getSpecie() + " este marcată ca si carnivoră dar nu e o specie cunoscută de plante carnivore");
            }
        }
    }
}
