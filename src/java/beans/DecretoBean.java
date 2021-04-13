package beans;

import security.Seguranca;
import dominio.Decreto;
import dominio.Pessoa;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;
import repository.DecretoRepository;
import LazyLoading.FiltroBusca;
import java.util.Date;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import service.DecretoService;
import util.FacesUtil;
import static util.FacesUtil.redirecionarPara;
import util.Utilitarios;

/**
 *
 * @author rosivaldo
 */
@Named
@ViewScoped
public class DecretoBean implements Serializable {

    @Inject
    private DecretoService decretoService;
    @Inject
    private DecretoRepository decretoRep;

    private UploadedFile upFile;
    private CroppedImage croppeFoto;
    private String imageTemp = "";
    private String pdfTemp = "";

    private Pessoa usuarioLogado = new Pessoa();
    private final Seguranca seguranca = new Seguranca();

    private String confSenha;

    private Decreto decreto;

    private List<Decreto> decretos;

    private List<Decreto> decretosFiltrados;

    private List<Decreto> ultimosDecretos;

    private FiltroBusca filtro = new FiltroBusca();
    private LazyDataModel<Decreto> model;
    private FacesContext facesContext;
    private Decreto ultimoDecreto;
    private Integer qtdDecretos;

    public DecretoBean() {
        decreto = new Decreto();
        decretos = new ArrayList();
        usuarioLogado = seguranca.getUsuario();

        model = new LazyDataModel<Decreto>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Object getRowKey(Decreto decreto) {
                return decreto.getNumero();
            }

            @Override
            public Decreto getRowData(String rowKey) {
                for (Decreto decreto : model) {
                    if (decreto.getNumero().toString().equals(rowKey)) {
                        return decreto;
                    }
                }

                return null;
            }

            @Override
            public List<Decreto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                getFiltro().setPrimeiroRegistro(first);
                getFiltro().setQuantidadeRegistros(pageSize);
                getFiltro().setAscendente(SortOrder.ASCENDING.equals(sortOrder));
                getFiltro().setPropriedadeOrdenacao(sortField);
                getFiltro().setFilters(filters);

                setRowCount(decretoRep.quantidadeFiltrados(getFiltro()));

                return decretoRep.filtrados(getFiltro());
            }

        };
    }

    @PostConstruct
    public void init() {
    }

    @PreDestroy
    public void saindo() {
        limpaArquivoTemporario();
    }
    
    public Date getDataAtual() {
        return new Date();
    }

    public void limparCamposBusca(TabChangeEvent event) {
        decreto = new Decreto();
        filtro = new FiltroBusca();
    }
    public void filtraDecretosIndex() {

        getFiltro().setDescricao(decreto.getConteudo());
        getFiltro().setNumero(decreto.getNumero());
        getFiltro().setAscendente(false);

        //setQtdDecretos(decretoRep.quantidadeFiltrados(getFiltro()));
        decretosFiltrados = decretoRep.criarCriteriaParaFiltroIndex(getFiltro());
    }

    public void onRowSelect(SelectEvent event) throws IOException {
        decreto = (Decreto) event.getObject();
        redirecionarPara(FacesUtil.getExternalContext().getRequestContextPath() + "/decreto/cadastro.xhtml?decreto=" + decreto.getNumero());
    }

    public void excluirDecreto(Decreto decreto) {
        decretoService.remover(decreto);
    }

    public void desativarDecreto() {
        decretoService.desativar(decreto);
        FacesUtil.addInfoMessage("Decreto desativado com sucesso!");
    }

    public void inicializarDecretos() throws IOException {
        if (!FacesUtil.isPostback()) {
            decretos = decretoService.listarTodos();
            decretosFiltrados = decretos;
        }
    }

    public void inicializarUltimoDecreto() throws IOException {
        if (!FacesUtil.isPostback()) {
            ultimoDecreto = decretoService.buscaUltimoDecreto();
        }
    }

    public void inicializarUltimosDecretos() throws IOException {
        if (!FacesUtil.isPostback()) {
            ultimosDecretos = decretoService.buscaUltimosDecretos();
            ultimoDecreto = ultimosDecretos.get(0);
            ultimosDecretos.remove(0);
        }
    }

    public String getDataDecretoExtenso() {
        if (decreto.getId() != null) {
            return Utilitarios.formataDataBRExtenso(decreto.getDataDecreto());
        } else {
            return "";
        }
    }

    public List<Decreto> completaNome(String query) {
        List<Decreto> sugestoes = decretoService.completaNome(query);
        return sugestoes;
    }

    public void salvarDecreto() {
        if (decreto.getId() != null) {
            atualizarDecreto();
        } else {
            cadastrarDecreto();
        }
        pdfTemp = "";
    }

    public void cadastrarDecreto() {

        try {
            decreto.setPessoa(usuarioLogado);
            decreto = decretoService.cadastrar(decreto);
            actionGuardarArquivo();
            FacesUtil.redirecionarPara("/decretos");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addErrorMessage("Erro ao salvar informações!");
        }
    }

    public void atualizarDecreto() {
        try {
            decreto = decretoService.atualizar(decreto);
            if (!this.getImageTemp().isEmpty()) {
                actionGuardarArquivo();
            }
            FacesUtil.redirecionarPara("/decretos");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addErrorMessage("Erro ao salvar informações!");
        }
    }

    public void filtraDecretos() {
        decretosFiltrados = decretoService.listarTodos();
        decretosFiltrados = decretoService.filtraConteudo(decretosFiltrados, decreto.getConteudo());
        decretosFiltrados = decretoService.filtraNumero(decretosFiltrados, decreto.getNumero());
    }

    //Atualizando contexto para limpar cache do pdfView
    public void atualizaContexto() {
        facesContext = FacesContext.getCurrentInstance();
    }

    // ####### UPLOAD DE ARQUIVO PDF ########
    public void carregaArquivo(FileUploadEvent event) {
        upFile = event.getFile();
    }

    public boolean uploadArquivo(FileUploadEvent event) {
        try {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/temp");
            String archivo = path + File.separator + event.getFile().getFileName();
            pdfTemp = "/resources/temp/" + event.getFile().getFileName();

            InputStream inputStream;
            try (FileOutputStream fileOutputStream = new FileOutputStream(archivo)) {
                byte[] buffer = new byte[6124];
                int bulk;
                inputStream = event.getFile().getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }
            }
            inputStream.close();
            this.setImageTemp(event.getFile().getFileName());

        } catch (IOException e) {
            e.printStackTrace();
            util.FacesUtil.addErrorMessage("Erro ao subir arquivo");
            return false;
        }
        return true;
    }

    public void actionFoto() {
        this.croppeFoto = null;
        this.imageTemp = null;
    }

    public void actionGuardarArquivo() {
        String relativePath = "/resources/pdf";
        String nomeArquivo = "decreto_" + decreto.getNumero().toString() + ".pdf";
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(relativePath);
        String archivo = path + File.separator + nomeArquivo;
        try {

            InputStream inputStream;
            try (OutputStream outStream = new FileOutputStream(new File(archivo))) {
                inputStream = new FileInputStream(path + "../../temp/" + this.getImageTemp());
                byte[] buffer = new byte[6124];
                int bulk;
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    outStream.write(buffer, 0, bulk);
                    outStream.flush();
                }
            }
            inputStream.close();

            decreto.setLinkArquivo(relativePath + File.separator + nomeArquivo);
            decretoService.atualizar(decreto);
            limpaArquivoTemporario();
            actionFoto();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void limpaArquivoTemporario() {
        if (!this.getImageTemp().isEmpty()) {
            String pathTemp = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/temp");
            String archivoTemp = pathTemp + File.separator + this.getImageTemp();
            File arquivoTemp = new File(archivoTemp);
            arquivoTemp.delete();
        }
    }

    /**
     * @return the decreto
     */
    public Decreto getDecreto() {
        return decreto;
    }

    /**
     * @param decreto the decreto to set
     */
    public void setDecreto(Decreto decreto) {
        this.decreto = decreto;
    }

    /**
     * @return the decretos
     */
    public List<Decreto> getDecretos() {
        return decretos;
    }

    /**
     * @param decretos the decretos to set
     */
    public void setDecretos(List<Decreto> decretos) {
        this.decretos = decretos;
    }

    /**
     * @return the decretoService
     */
    public DecretoService getDecretoRep() {
        return decretoService;
    }

    /**
     * @param decretoService the decretoService to set
     */
    public void setDecretoRep(DecretoService decretoService) {
        this.decretoService = decretoService;
    }

    /**
     * @return the upFile
     */
    public UploadedFile getUpFile() {
        return upFile;
    }

    /**
     * @param upFile the upFile to set
     */
    public void setUpFile(UploadedFile upFile) {
        this.upFile = upFile;
    }

    /**
     * @return the decretosFiltrados
     */
    public List<Decreto> getDecretosFiltrados() {
        return decretosFiltrados;
    }

    /**
     * @param decretosFiltrados the decretosFiltrados to set
     */
    public void setDecretosFiltrados(List<Decreto> decretosFiltrados) {
        this.decretosFiltrados = decretosFiltrados;
    }

    /**
     * @return the croppeFoto
     */
    public CroppedImage getCroppeFoto() {
        return croppeFoto;
    }

    /**
     * @param croppeFoto the croppeFoto to set
     */
    public void setCroppeFoto(CroppedImage croppeFoto) {
        this.croppeFoto = croppeFoto;
    }

    /**
     * @return the imageTemp
     */
    public String getImageTemp() {
        return imageTemp;
    }

    /**
     * @param imageTemp the imageTemp to set
     */
    public void setImageTemp(String imageTemp) {
        this.imageTemp = imageTemp;
    }

    /**
     * @return the confSenha
     */
    public String getConfSenha() {
        return confSenha;
    }

    /**
     * @param confSenha the confSenha to set
     */
    public void setConfSenha(String confSenha) {
        this.confSenha = confSenha;
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

    /**
     * @return the model
     */
    public LazyDataModel<Decreto> getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(LazyDataModel<Decreto> model) {
        this.model = model;
    }

    /**
     * @return the pdfTemp
     */
    public String getPdfTemp() {
        return pdfTemp;
    }

    /**
     * @param pdfTemp the pdfTemp to set
     */
    public void setPdfTemp(String pdfTemp) {
        this.pdfTemp = pdfTemp;
    }

    /**
     * @return the facesContext
     */
    public FacesContext getFacesContext() {
        atualizaContexto();
        return facesContext;
    }

    /**
     * @param facesContext the facesContext to set
     */
    public void setFacesContext(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    /**
     * @return the ultimoDecreto
     */
    public Decreto getUltimoDecreto() {
        return ultimoDecreto;
    }

    /**
     * @param ultimoDecreto the ultimoDecreto to set
     */
    public void setUltimoDecreto(Decreto ultimoDecreto) {
        this.ultimoDecreto = ultimoDecreto;
    }

    /**
     * @return the ultimosDecretos
     */
    public List<Decreto> getUltimosDecretos() {
        return ultimosDecretos;
    }

    /**
     * @param ultimosDecretos the ultimosDecretos to set
     */
    public void setUltimosDecretos(List<Decreto> ultimosDecretos) {
        this.ultimosDecretos = ultimosDecretos;
    }

    /**
     * @return the qtdDecretos
     */
    public Integer getQtdDecretos() {
        return qtdDecretos;
    }

    /**
     * @param qtdDecretos the qtdDecretos to set
     */
    public void setQtdDecretos(Integer qtdDecretos) {
        this.qtdDecretos = qtdDecretos;
    }

}
