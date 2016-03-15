package py.pol.una.ii.pw.util;

import java.util.Date;
import java.util.List;

public class Venta {
	private Integer cliente;
	private Date fecha;
	private Float monto;
	
	private List<VentaDet> ventaDetalle;
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public List<VentaDet> getVentaDetalle() {
		return ventaDetalle;
	}
	public void setVentaDetalle(List<VentaDet> ventaDetalle) {
		this.ventaDetalle = ventaDetalle;
	}
	public Integer getCliente() {
		return cliente;
	}
	public void setCliente(Integer cliente) {
		this.cliente = cliente;
	}
	public Float getMonto() {
		return monto;
	}
	public void setMonto(Float monto) {
		this.monto = monto;
	}
}