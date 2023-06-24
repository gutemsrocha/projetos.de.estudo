package com.devsuperior.dscommerce.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.dto.OrderItemDTO;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.OrderStatus;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthService authService;

	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {
		Order order = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		authService.validateSelfOrAdmin(order.getClient().getId());// Teste se o usuário que está logado
		// é ADMIN ou o DONO do pedido.
		return new OrderDTO(order);
	}

	@Transactional
	public OrderDTO insert(OrderDTO dto) {
		// 1) CRIAR O ORDER
		Order order = new Order();
		// 2) QUAIS SÃO OS DADOS DO PEDIDO? Cliente, Id, Pagamento, Momento, Status?
		// 3) QUEM SERÁ O MOMENT? O INSTANTE DO PEDIDO? SERÁ O INSTANTE ATUAL DO
		// SISTEMA. ENTÃO:
		// o ".setMoment" deverá trazer o instante atual "(Instant.now())";
		order.setMoment(Instant.now());
		// 4) QUAL É O STATUS DE UM PEDIDO QUE ACABOU DE SER SALVO?
		// ELE ESTARÁ AGUARDANDO PAGAMENTO, ENTÃO:
		order.setStatus(OrderStatus.WAITING_PAYMENT);
		// 5) AGORA ESTÁ FALTANDO O CLIENTE. QUEM SERÁ O CLIENTE?
		// SERÁ O USUÁRIO LOGADO
		// NO USER SERVICE NÓS JÁ TEMOS UM MÉTODO CHAMADO "authenticated" que pega o
		// usuário logado.
		// 6) DEVEMOS INJETAR O UserService userservice lá em cima.
		// 7) IREMOS PEGAR UMA INSTÂNCIA DE USUÁRIO: "User user ="
		// QUE IRÁ RECEBER O "userService.authenticated();". FICANDO ASSIM:
		User user = userService.authenticated();
		// 8) AGORA É SÓ DEFINIR QUE O "order.setCliente()" será este usuário "user"
		// acima. FICANDO ASSIM
		order.setClient(user);
		// 9) E AGORA O QUE IREMOS FAZER COM O "dto" LÁ DO ARGUMENTO DA FUNÇÃO INSERT?
		// ELE IRÁ TRAZER EXATAMENTE QUAIS SÃO OS ITENS... Produto e quantidade de cada
		// item do carrinho.
		// PARA ISTO TEREMOS QUE INSTANCIAR UNS ITENS DE PEDIDO.
		// NO MODELO DE DADOS DO NOSSO DOCUMENTO, O PEDIDO "Order" TEM ÍTEM DE PEDIDO
		// "OrderItem" QUE POR
		// SUA VEZ ESTÁ ACOSSIADO COM UM PRODUTO "Product".

		// o ITEM DE PEDIDO "OrderItem" TEM A QUANTIDADE E O PREÇO. ESTE PREÇO, DEVEMOS
		// COPIAR DO PRODUTO.

		// 10) TEREMOS QUE VARRER A LISTA DE ITENS DO DTO com o For

		// Para cada "OrderItemDTO" que chamaremos de "itemDto"
		// Dentro do "dto.getItems()"
		// Iremos varrer todos os itens que vierem dentro desta requisição.
		// 11) DEVEMOS INJETAR O "ProductRepository productRepository" LÁ EM CIMA.
		for (OrderItemDTO itemDto : dto.getItems()) {
			// 12) CRIAR UMA VARIÁVEL "product" DO TIPO "Product" => "Product product" QUE
			// RECEBERÁ
			// "= productRepository.getReferenceById(itenDto.getProductId())"
			// DESTA FORMA PEGAMOS O ID DO PRODUTO E INSTANCIO UM OBJETO PRODUCT UMA
			// REFERENCIA A PARTIR DESTE ID.
			Product product = productRepository.getReferenceById(itemDto.getProductId());

			// AGORA PRECISAMOS INSTANCIAR O "OrderItem" QUE TEM UMA REFERENCIA PARA O
			// PEDIDO PARA O PRODUTO,
			// QUANTIDADE E PREÇO.
			// INCLUSIVE A ENTIDADE "OrderItem" POSSUI UM CONSTRUTOR COM
			// OS DADOS QUE PRECISAMOS: order, product, quantity, price

			// 13) CRIAR O "OrderItem item" QUE IRÁ RECEBER "new OrderItem(order -> que é
			// este objeto que estamos
			// montando (o da linha 41) , o product -> que é o produto (da linha 83) ,
			// a quantidade que já está no "itemDto" PORTANTO SERÁ "itemDto.getQuatity()",
			// E POR ÚLTIMO O PREÇO QUE FICARÁ ASSIM: "product.getPrice()" . ENTÃO:
			OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());
			// 14) ASSOCIAR O novo "OrderItem" com o novo "Order" da linha 42.
			order.getItems().add(item);

		}

		repository.save(order);
		orderItemRepository.saveAll(order.getItems());

		return new OrderDTO(order);
	}

}
