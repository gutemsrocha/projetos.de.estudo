package com.devsuperior.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.OrderStatus;

public class OrderDTO {

	private Long id;
	private Instant moment;
	private OrderStatus status;

	private ClientDTO client;

	private PaymentDTO payment;

	@NotEmpty(message = "Deve ter pelo menos um item")
	private List<OrderItemDTO> items = new ArrayList<>();

	public OrderDTO(Long id, Instant moment, OrderStatus status, ClientDTO client, PaymentDTO payment) {
		this.id = id;
		this.moment = moment;
		this.status = status;
		this.client = client;
		this.payment = payment;
	}

	public OrderDTO(Order entity) {
		id = entity.getId();
		moment = entity.getMoment();
		status = entity.getStatus();

		// QUEM VAI SER O CLIENT?
		// Vai ser o ClientDTO
		// Iremos utilizar o Construtor do ClientDTO => public ClientDTO(User entity)
		// Para puxar o ".getName()
		// Para isto teremos que instanciar com o new. Ficando como abaixo:
		client = new ClientDTO(entity.getClient());

		// NO CASO DO PAGAMENTO, O MESMO PODERÁ SER NULO
		// Portanto teremos que utilizar um SE (IF)
		// Se entity.getPayment() == null
		// então "?" jogaremos null no pagamento também
		// Caso contrário aí sim iremos instanciar um novo pagamento como new
		// Ficando como abaixo:
		payment = (entity.getPayment() == null) ? null : new PaymentDTO(entity.getPayment());
	
		// UM FOR PARA COPIAR OS DADOS DA ENTIDADE PARA O DTO
		//Para cada item: "Order Item item"
		//Que tiver dentro do pedido: "entity.getItems()"
		for (OrderItem item : entity.getItems()) {
			OrderItemDTO itemDTO = new OrderItemDTO(item);
			items.add(itemDTO);
			
		}
	
	}

	public Long getId() {
		return id;
	}

	public Instant getMoment() {
		return moment;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public ClientDTO getClient() {
		return client;
	}

	public PaymentDTO getPayment() {
		return payment;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}
	
	public Double getTotal() {
		double sum = 0.0;
		for (OrderItemDTO item : items) {
			sum += item.getSubTotal();
		}
		return sum;
	}

}
