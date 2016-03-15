package py.pol.una.ii.pw.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "VENTAS_DET")
public class VentaDetalle implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id_ventaDetalle;

	@ManyToOne
	@JoinColumn(name = "id_producto")
	private Producto producto;

	@Column(columnDefinition = "numeric CHECK (cantidad > 0)")
	private Float cantidad;
	private Float monto_parcial;

	@ManyToOne
	@JoinColumn(name = "id_ventaCabecera")
	private VentaCabecera ventaCabecera;

	public Integer getId_ventaDetalle() {
		return id_ventaDetalle;
	}

	public void setId_ventaDetalle(Integer id_ventaDetalle) {
		this.id_ventaDetalle = id_ventaDetalle;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Float getCantidad() {
		return cantidad;
	}

	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}

	public Float getMonto_parcial() {
		return monto_parcial;
	}

	public void setMonto_parcial(Float monto_parcial) {
		this.monto_parcial = monto_parcial;
	}

	public VentaCabecera getVentaCabecera() {
		return ventaCabecera;
	}

	public void setVentaCabecera(VentaCabecera ventaCabecera) {
		this.ventaCabecera = ventaCabecera;
	}
}
