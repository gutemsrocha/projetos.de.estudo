package com.devsuperior.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {
	
	@Autowired
	private UserService userService;
	
	/* EXPLICAÇÃO DO TESTE ABAIXO
	 * 1) PEGUEI O USUÁRIO AUTENTICADO E SALVEI NA VARIAL "me" - User me = userService.authenticated();
	 * 2) SE ESTE USUÁRIO "me" NÃO É ADMIN -> "if(!me.hasRole("ROLE_ADMIN")"   
	 * 		E TAMBÉM "&&" ESTE USUÁRIO "me" NÃO É O MESMO USUÁRIO DESTE "id" QUE FOI ENVIADO
	 * NO ARGUMENTO DA FUNÇÃO, O PARÂMETRO "userId".
	 * SE ISTO FOR VERDADE QUER DIZER QUE O USUÁRIO NÃO É ADMIN E NEM O PROPRIO USUÁRIO DO ARGUMENTO 
	 * QUE CHEGOU NO MÉTODO. 
	 * 
	 * 3) ENTÃO NESTE CASO LANÇAREMOS UMA EXCEÇÃO.*/
	
	public void validateSelfOrAdmin(long userId) {
		User me = userService.authenticated();
		if(!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied");
		}
		
	}
	
	

}
