package com.devsuperior.dscommerce.entities;

import javax.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(columnDefinition = "TEXT")
	private String description;
	private Double price;
	private String imgUrl;

	@ManyToMany
	@JoinTable(name = "tb_product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();

	@OneToMany(mappedBy = "id.product")
	private Set<OrderItem> items = new HashSet<>();

	public Product() {
	}

	public Product(Long id, String name, String description, Double price, String imgUrl) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public Set<OrderItem> getItems() {
		return items;
	}

	public List<Order> getOrders() {
		return items.stream().map(x -> x.getOrder()).toList();
	}
	/*
	 * EXPLICAÇÃO DA FUNÇÃO ACIMA:
	 * 
	 * public List<Order> getOrders() { return items.stream().map(x ->
	 * x.getOrder()).toList(); }
	 * 
	 * 1º) Devemos pegar o items, que é a Lista do tipo OrderItems, ou seja, é um
	 * Set de OrderItems, 2º) Em seguida, aplicaremos um ".stream().map() 3º) .map(x
	 * -> x.getOrder()) : Para cada objeto "x" do tipo OrderItem, devemos chamar o
	 * x.getOrder(), porque dessa forma conseguimos pegar apenas o order que está
	 * dentro do OrderItem. 4º) Por ultimo aplicaremos um ".toList()" para retornar
	 * para lista.
	 * 
	 */

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(id, other.id);
	}
}
