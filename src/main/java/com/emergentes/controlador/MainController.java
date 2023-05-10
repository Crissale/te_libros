package com.emergentes.controlador;

import com.emergentes.utiles.conexionDB;
import com.emergentes.modelo.libro;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String op;
            op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";
            ArrayList<libro> lista = new ArrayList<libro>();
            conexionDB canal = new conexionDB();
            Connection conn = canal.conectar();
            PreparedStatement ps;
            ResultSet rs;
            if (op.equals("list")){
                //Para listar los datos
                String sql = "select * from Libros";
                //Consulta de seleccion y almacenarlo en una coleccion
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while   (rs.next()){
                    libro lib = new libro();
                    lib.setId(rs.getInt("id"));
                    lib.setIsbn(rs.getString("isbn"));
                    lib.setTitulo(rs.getString("titulo"));
                    lib.setCategoria(rs.getString("categoria"));
                    lista.add(lib);
                }
                request.setAttribute ("lista", lista);
                //Enviar al index.jsp para mostrat la informacion
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
            if (op.equals("nuevo")) {
                //instanciar un objeto de la clase Libro
                libro li = new libro();
                System.out.println(li.toString());
                //El objeto se pose como atributo de request
                request.setAttribute("libro", li);
                //Redireccionar  a editar.jsp
                request.getRequestDispatcher("editar.jsp").forward (request, response);    
            }
            if (op.equals("editar")) {
                int id = Integer.parseInt(request.getParameter("id"));   
                String sql = "select * from librosemer where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                libro li = new libro(); 
                while   (rs.next()){
                    li.setId(rs.getInt("id"));
                    li.setIsbn(rs.getString("isbn"));
                    li.setTitulo(rs.getString("titulo"));
                    li.setCategoria(rs.getString("categoria"));
                }
                request.setAttribute("libro", li);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
            }
            if (op.equals("eliminar")){
                //obtener el id
                int id = Integer.parseInt(request.getParameter("id"));
                //Realizar la eliminacion en la base de datos
                String sql = "delete from libros where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                //Redireccionar a MainController
                response.sendRedirect("MainController");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR AL CONECTAR  "+ ex.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String isbn = request.getParameter("isbn");
            String titulo = request.getParameter("titulo");
            String categoria = request.getParameter("categoria");
            
            libro lib = new libro();
            lib.setId(id);
            lib.setIsbn(isbn);
            lib.setTitulo(titulo );
            lib.setCategoria(categoria);
            
            conexionDB canal = new conexionDB();
            Connection conn = canal.conectar();
            PreparedStatement ps;
            ResultSet rs;
            if (id == 0){
                //Nuevo registro
                String sql = "insert into libros (isbn,titulo,categoria) values (?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString (1, lib.getIsbn());
                ps.setString (2, lib.getTitulo());
                ps.setString (3, lib.getCategoria());
                ps.executeUpdate();
                
                
            } 
                    
            //si el registro ya existe
            else{
                String sql = "update libros set isbn=?,titulo=?,categoria=? where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString (1, lib.getIsbn());
                ps.setString (2, lib.getTitulo());
                ps.setString (3, lib.getCategoria());
                ps.setInt(4, lib.getId());
                ps.executeUpdate();
            }
            response.sendRedirect("MainController");
        } catch (SQLException ex) {
            System.out.println("Error en SQL " + ex.getMessage());
        }
    }

}
