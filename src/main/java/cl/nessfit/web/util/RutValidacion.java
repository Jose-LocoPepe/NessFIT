package cl.nessfit.web.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import cl.nessfit.web.model.Usuario;


@Component
public class RutValidacion implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return Usuario.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Usuario usuario = (Usuario) target;

	// lógica para validar
	//se declaran las variables
	int rut,digito,suma,resto,resultado,factor;
	String digitoV= "K";
	String rutString = usuario.getRut();
	rut=1;
	//se eliminan puntos comas y el ultimo digito
	rutString.replaceAll(".", "");
	rutString.replaceAll("-", "");
	rutString = rutString.substring(0, rutString.length()-1);
	
	
	while(rut <= 0); //Iteración

	//Ahora viene la parte de extraer dígito por dígito el rut

	for(factor = 2, suma = 0; rut > 0; factor++){

	digito = rut % 10;
	rut /= 10;
	suma += digito * factor;

	if(factor >= 7) factor = 1; //Para volver al ciclo

	}
	// Ahora viene el algoritmo del módulo 11

	resto = suma % 11;
	resultado = 11 -resto;

	//Mostramos por pantalla.
	//Si el resultado es menor que 10, se imprime el número.
	//Si es igual a 10, entonces se imprime "K"
	//Si no, entonces es 0

	System.out.println("El digito verificador es: ");

	if(resultado < 10) System.out.println(resultado+"\n");
	else if (resultado == 10) System.out.println("K\n");
	else System.out.println("0\n");
	String rutU = usuario.getRut();

	// errors.rejectValue("rut", null, "rut no válido");

    }

}
