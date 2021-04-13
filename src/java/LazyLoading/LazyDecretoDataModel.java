/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LazyLoading;

import dominio.Decreto;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import repository.DecretoRepository;

/**
 *
 * @author rosivaldo.adm
 */
@Named
@ViewScoped
public class LazyDecretoDataModel extends LazyDataModel<Decreto> implements Serializable {

    @Inject
    private DecretoRepository decretoRep;

    private List<Decreto> datasource;
    private FiltroBusca filtro = new FiltroBusca();

    public LazyDecretoDataModel() {
    }

    @Override
    public Decreto getRowData(String rowKey) {
        for (Decreto decreto : datasource) {
            if (decreto.getNumero().toString().equals(rowKey)) {
                return decreto;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Decreto decreto) {
        return decreto.getNumero();
    }

    @Override
    public List<Decreto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        getFiltro().setPrimeiroRegistro(first);
        getFiltro().setQuantidadeRegistros(pageSize);
        getFiltro().setAscendente(SortOrder.ASCENDING.equals(sortOrder));
        getFiltro().setPropriedadeOrdenacao(sortField);
        getFiltro().setFilters(filters);

        setRowCount(decretoRep.quantidadeFiltrados(getFiltro()));

        return datasource = decretoRep.filtrados(getFiltro());
    }

    /**
     * @return the filtro
     */
    public FiltroBusca getFiltro() {
        return filtro;
    }

    /**
     * @param filtro the filtro to set
     */
    public void setFiltro(FiltroBusca filtro) {
        this.filtro = filtro;
    }
}
