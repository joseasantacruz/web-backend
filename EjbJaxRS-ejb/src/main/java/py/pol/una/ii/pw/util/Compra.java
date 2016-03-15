package py.pol.una.ii.pw.util;

import java.util.Date;
import java.util.List;

import py.pol.una.ii.pw.util.CompraDet;

public class Compra {

	private Integer proveedor;
	private Date fecha;
	private Float monto;
	private List<CompraDet> compraDetalle;
	
	
	public Integer getProveedor() {
		return proveedor;
	}
	public void setProveedor(Integer proveedor) {
		this.proveedor = proveedor;
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
	public List<CompraDet> getCompraDetalle() {
		return compraDetalle;
	}
	public void setCompraDetalle(List<CompraDet> compraDetalle) {
		this.compraDetalle = compraDetalle;
	}
}
