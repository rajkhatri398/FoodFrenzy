package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entities.Admin;
import com.example.demo.repositories.AdminRepository;

@Component
public class AdminServices
{
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	public List<Admin>getAll()
	{
		 List<Admin> admins = (List<Admin>)this.adminRepository.findAll();
		 return admins;
	}

	public Admin getAdmin(int id)
	{
		Optional<Admin> optional = this.adminRepository.findById(id);
		Admin admin=optional.get();
		return admin;
	}

	public void update(Admin admin ,int id)
	{
		for (Admin ad : getAll()) 
		{
			if(ad.getAdminId()==id)
			{
				this.adminRepository.save(admin);
			}
		}
	}
	
	public void delete(int id)
	{
		this.adminRepository.deleteById(id);
	}

	public void addAdmin(Admin admin)
	{
		if (admin.getAdminPassword() != null && !admin.getAdminPassword().startsWith("$2a$") && !admin.getAdminPassword().startsWith("$2b$") && !admin.getAdminPassword().startsWith("$2y$")) {
			admin.setAdminPassword(passwordEncoder.encode(admin.getAdminPassword()));
		}
		this.adminRepository.save(admin);
	}

	public boolean validateAdminCredentials(String email,String password)
	{
		Admin admin=adminRepository.findByAdminEmail(email);
		if (admin == null) {
			return false;
		}
		return passwordEncoder.matches(password, admin.getAdminPassword()) || admin.getAdminPassword().equals(password);
	}
}