/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

/**
 *
 * @author felipe
 */
public class ButtonImage extends javax.swing.JButton {
    
    //Imagens
    public final Icon bandeiraIcon = new javax.swing.ImageIcon(getClass().getResource("/images/bandeira.png"));
    public final Icon minaIcon = new javax.swing.ImageIcon(getClass().getResource("/images/mina.png"));
    public ImageIcon iconTriste = new ImageIcon(getClass().getResource("/images/Triste.png"));
    
    //Para facilitar na hora de gerar as posições das bombas
    private int codigo;
    private int i; //linha na qual o ButtonImage se encontra na tabela
    private int j;// coluna na qual o ButtonImage se encontra na tabela
       
    //Caracteristicas que o botão carrega
    private boolean campoBomba = false;         //Determina se tem bomba ou não 
    private int campoContadorMinas;                //Se tiver uma bomba em volta ele incrementa o contador 
    private boolean ocupado = false;            //variável que armazena se a posição já foi usada ou não, por uma bandeira de mina ou por botão ja escolhido
    private boolean marcadorDeMinas = false;    //Identificar se foi marcado minas com bandeiras ou não.
    private int quantBombasAoRedor = 0;
    private boolean ocupadoAux = false;

    
    
    public ButtonImage() {
        this.setPreferredSize(new Dimension(50, 50));
        this.addMouseListener(new MouseHandler());
        
        
        System.out.println("Chamou o construtor");
    }

    public boolean isOcupadoAux() {
        return ocupadoAux;
    }

    public void setOcupadoAux(boolean ocupadoAux) {
        this.ocupadoAux = ocupadoAux;
    }
    //Coordenadas---------------------------------------------------------------
   
   //Criar a partir do grid
    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void setPos(int i, int j) {
        this.i = i;
        this.j = j;
    }
    
    //--------------------------------------------------------------------------
    
    //Manipula Campo bomba------------------------------------------------------
    public void setCodigo(int x){
        this.codigo = x; 
    }
    
    public int getCodigo(){
        return codigo;
    }
    
    
    //Manipula Campo bomba------------------------------------------------------
    public void setCampoBomba(boolean x){
        this.campoBomba = x; 
    }
    
    public boolean getCampoBomba(){
        return campoBomba;
    }
    
    //Contador de bombas ao redor de um botão-----------------------------------
    private void setCampoContadorMinas(int x){
        this.campoContadorMinas = x; 
    }
    
    private int getCampoContadorMinas(){
        return campoContadorMinas;
    }
    
    //Se o botão esta ocupdo ou não---------------------------------------------
    public boolean getOcupado(){
        return ocupado;
    }
    
    public void setOcupado(boolean x){
        this.ocupado = x;
    }
    //Quantidade de bombas ao redor---------------------------------------------
    public int getQuantBombasAoRedor(){
        return quantBombasAoRedor;
    }
    
    public void setQuantBombasAoRedor(int x){
        this.quantBombasAoRedor = x;
    }
    
    //Campo Marcador de bandeiras minas-----------------------------------------
    public boolean getMarcadorDeMinas(){
        return marcadorDeMinas;
    }
    
    //Envia imagem da bandeira da mina------------------------------------------
    private void setBandeira() {
        this.setIcon(bandeiraIcon);
    }
    
    //Envia imagem da mina------------------------------------------------------
    private void setMina() {
        this.setIcon(minaIcon);
    }

    public void setResetBotao(){
        this.campoBomba = false;
        this.campoContadorMinas = 0;
        this.ocupado = false;
        this.marcadorDeMinas = false;
        this.setIcon(null);
        this.setBackground(null);
        this.setText("");
        this.setOcupadoAux(false);
    }
    
    public void revelaBotao(){
        if(getOcupado()==false){
            if(getCampoBomba() == true){
                this.setMina();
                this.setBackground(Color.GRAY);
                this.ocupado = true;
            }
            
            else if(getQuantBombasAoRedor() > 0){
                this.setBackground(Color.blue);
                this.setText(""+this.getQuantBombasAoRedor());
                this.ocupado = true;
            }
            else{
                this.setBackground(Color.GRAY); 
                this.ocupado = true;
            }    
        }
        
    }
    
   /**
     * Classe interna responsável por lidar com os eventos do mouse
     */
    private class MouseHandler extends MouseAdapter {

        /**
         * Manipula o evento de quando o mouse é solto depois do clique
         *
         * @param event Objeto com os dados do evento
         */
       
        public void mouseClicked(MouseEvent event) {
          
            ButtonImage botao = (ButtonImage) event.getComponent(); 
            
            if(botao.getOcupado()== false){
                
                //Eventos do botão esquerdo
                 if(event.getButton() == MouseEvent.BUTTON1){
                    if(botao.marcadorDeMinas == false){
                        System.out.println("esquerdo");
                        botao.setOcupado(true);
                        
                        
                        //Se clicou em uma bomba, jogo acaba
                        if(getCampoBomba()==true){
                            botao.setMina();
                            botao.setBackground(Color.red);
                            Game.carinha.setIcon(iconTriste);
                            Game.revelarTodosOsCampos();
                            Game.perdeuJogo();
                            
                        }
                        
                                            
                        else{
                            //Se clicou em um botão vazio, revela adjacencias
                            if(botao.getQuantBombasAoRedor() == 0){
                                botao.setBackground(Color.GRAY);
                                Game.revelarParcialmenteOsCampos(botao.getI(), botao.getJ());
                                
                            }
                            //Se não, revela apenas botoes que contem numeros, nessse caso, revela apenas um botão
                            else{
                                botao.setBackground(Color.green);
                                botao.setText(""+botao.getQuantBombasAoRedor());
                            }
                            
                        }
                        
                        Game.verificaVitoria();
                            
                   }    
                }
                 
                //Eventos do botao direito 
                else if(event.getButton() == MouseEvent.BUTTON3){
                     System.out.println("direito");
                     //Marca mina
                     if(botao.marcadorDeMinas == false){
                         if(Game.contadorDeMinas > 0){
                            //aux.setText("M");
                            botao.setBandeira();
                            botao.marcadorDeMinas = true;
                         }    
                     }
                     //Desmarca mina
                     else{
                         //aux.setText("");
                         botao.setIcon(null);
                         botao.setBackground(null);
                         botao.marcadorDeMinas = false;
                         
                     }
                     
                     Game.contadorDeMinas();
                     Game.caixaTxtMinasCont.setText("" + Game.contadorDeMinas);
                }
            }
        } 
    }
}
