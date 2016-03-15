package py.pol.una.ii.pw.util;

import java.util.Date;

public class PagoJson {
	private Integer cliente;
	private Date fecha;
	private Float monto;
	
	public Integer getCliente() {
		return cliente;
	}
	public void setCliente(Integer id_cliente) {
		this.cliente = id_cliente;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Float getMonto() {
		return monto;
	}
	public void setMonto(Float monto) {
		this.monto = monto;
	}
}
