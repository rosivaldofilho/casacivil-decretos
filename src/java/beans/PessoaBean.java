package beans;

import security.Seguranca;
import dominio.Pessoa;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
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
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.UploadedFile;
import service.PessoaService;
import util.FacesUtil;

/**
 *
 * @author rosivaldo
 */
@Named
@ViewScoped
public class PessoaBean implements Serializable {

    @Inject
    private PessoaService pessoaService;

    private UploadedFile upFile;
    private CroppedImage croppeFoto;
    private String imageTemp;

    private Pessoa usuarioLogado = new Pessoa();
    private final Seguranca seguranca = new Seguranca();

    private String confSenha;

    private Pessoa pessoa;

    private List<Pessoa> pessoas;

    private List<Pessoa> pessoasFiltrados;

    public PessoaBean() {
        pessoa = new Pessoa();
        pessoas = new ArrayList();
        usuarioLogado = seguranca.getUsuario();
    }

    @PostConstruct
    public void init() {
    }

    public void excluirPessoa(Pessoa pessoa) {
        pessoaService.remover(pessoa);
    }

    public void desativarPessoa() {
        pessoaService.desativar(pessoa);
        FacesUtil.addInfoMessage("Pessoa desativado com sucesso!");
    }

    public void inicializarPessoas() throws IOException {
        if (!FacesUtil.isPostback()) {
            pessoas = pessoaService.listarTodos();
        }
    }

    public List<Pessoa> completaNome(String query) {
        List<Pessoa> sugestoes = pessoaService.completaNome(query);
        return sugestoes;
    }

    public void salvarPessoa() {
        if (pessoa.getId() != null) {
            atualizarPessoa();
        } else {
            cadastrarPessoa();
        }
    }

    public void cadastrarPessoa() {

        try {
            pessoaService.cadastrar(pessoa);
            FacesUtil.addInfoMessage("Usuário Cadastrado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addErrorMessage("Erro ao salvar informações!");
        }
    }
    
    public void atualizarPessoa() {
        try {
            pessoaService.atualizar(pessoa);
            FacesUtil.addInfoMessage("Informações salvas com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addErrorMessage("Erro ao salvar informações!");
        }
    }
    
    public void alterarSenhaAdm() {
        try {
            pessoaService.alterarSenha(pessoa);
            FacesUtil.addInfoGrowl("Informações salvas com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addErrorMessage("Erro ao salvar informações!");
        }
    }

    

    // ####### UPLOAD DA FOTO DE PERFIL ########
    public void actionFoto() {
        this.croppeFoto = null;
        this.imageTemp = null;
    }

    public void limpaArquivoTemporario() {
        String pathTemp = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/temp");
        String archivoTemp = pathTemp + File.separator + this.getImageTemp();
        File arquivoTemp = new File(archivoTemp);
        arquivoTemp.delete();
    }

    public void actionGuardarFoto() {
        String relativePath = "/resources/images/profiles/user";
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(relativePath);
        String archivo = path + File.separator + "pessoa_" + pessoa.getId() + ".jpg";
        try {

            if (croppeFoto != null) {
                try (FileImageOutputStream imageOutput = new FileImageOutputStream(new File(archivo))) {
                    imageOutput.write(croppeFoto.getBytes(), 0, croppeFoto.getBytes().length);

                    BufferedImage originalImage = ImageIO.read(new File(archivo));
                    int width = 200;
                    int height = 200;
                    BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    resizedImage.getGraphics().drawImage(originalImage, 0, 0, width, height, null);
                    ImageIO.write(resizedImage, "jpg", new File(archivo));
                    limpaArquivoTemporario();

                    pessoa.setFoto(relativePath + File.separator + "pessoa_" + pessoa.getId() + ".jpg");
                    System.out.println("###############" + pessoa.getFoto());
                }
            } else {
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
            }

            actionFoto();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(FileUploadEvent event) {
        try {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/temp");
            String archivo = path + File.separator + event.getFile().getFileName();

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
        }
    }

    public void upload() throws IOException {
        if (getUpFile() != null) {
            InputStream in = new BufferedInputStream(getUpFile().getInputstream());
            String relativeWebPath = "/resources/images/profiles/user/";
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().
                    getExternalContext().getContext();

            String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);
            String absolutePathSemBuild = absoluteDiskPath.replace("\\build", "");

            // Cria o diretório caso ele não exista
            File diretorio = new File(absolutePathSemBuild);
            if (!diretorio.exists()) {
                diretorio.mkdir();
            }

            File file = new File(diretorio, getUpFile().getFileName());

            String caminho = file.getAbsolutePath();
            System.out.println(caminho);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                while (in.available() != 0) {
                    fos.write(in.read());
                }
                // guardou o arquivo com sucesso
                pessoa.setFoto(relativeWebPath + getUpFile().getFileName());
                FacesMessage message = new FacesMessage("Sucesso", getUpFile().getFileName() + " foi enviado");
                FacesContext.getCurrentInstance().addMessage(null, message);

            } catch (Exception e) {
                e.getMessage();
            }
        }

    }

    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    /**
     * @return the pessoas
     */
    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    /**
     * @param pessoas the pessoas to set
     */
    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    /**
     * @return the pessoaService
     */
    public PessoaService getPessoaRep() {
        return pessoaService;
    }

    /**
     * @param pessoaService the pessoaService to set
     */
    public void setPessoaRep(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
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
     * @return the pessoasFiltrados
     */
    public List<Pessoa> getPessoasFiltrados() {
        return pessoasFiltrados;
    }

    /**
     * @param pessoasFiltrados the pessoasFiltrados to set
     */
    public void setPessoasFiltrados(List<Pessoa> pessoasFiltrados) {
        this.pessoasFiltrados = pessoasFiltrados;
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

}
