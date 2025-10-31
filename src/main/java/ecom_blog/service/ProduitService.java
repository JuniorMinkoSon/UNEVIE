package ecom_blog.service;

import ecom_blog.model.Produit;
import ecom_blog.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {
    @Autowired private ProduitRepository produitRepo;

    public List<Produit> getAll() { return produitRepo.findAll(); }

    public Produit save(Produit p) { return produitRepo.save(p); }

    public void delete(Long id) { produitRepo.deleteById(id); }

    public Produit findById(Long id) { return produitRepo.findById(id).orElse(null); }
}
