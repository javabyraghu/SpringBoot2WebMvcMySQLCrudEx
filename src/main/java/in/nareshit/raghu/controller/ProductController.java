package in.nareshit.raghu.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.nareshit.raghu.model.Product;
import in.nareshit.raghu.service.IProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private IProductService service; //HAS-A
	
	//1. show Register page
	@GetMapping("/register")
	public String showReg() {
		return "ProductRegister";	
	}
	
	//2. save on click submit
	@PostMapping("/save")
	public String saveProduct(
			@ModelAttribute Product product,
			Model model
			) 
	{
		Integer id = service.saveProduct(product);
		String msg ="Product created with id "+id;
		model.addAttribute("message", msg);
		return "ProductRegister";
	}
	
	//3. display all products
	/***
	 * Make call to service Layer to fetch data
	 * Read data as List<T>
	 * Send data to UI using Model memory
	 */
	@GetMapping("/all")
	public String showAll(Model model) {
		List<Product> list = service.getAllProducts();
		model.addAttribute("list", list);
		return "ProductData";
	}
	
	
	//4. delete product by id
	/**
	 * On Click HyperLink request looks
	 *   /delete?id=<value>.
	 *   Read this using Request Param
	 *   call service for delete
	 *   Load new data and create msg send to UI
	 *   
	 */
	@GetMapping("/delete")
	public String removeProduct(
			@RequestParam Integer id,
			Model model
			)
	{
		service.deleteProduct(id);
		//create success msg
		String msg = "Product '"+id+"' deleted!";
		model.addAttribute("message", msg);
		
		//load new data
		List<Product> list = service.getAllProducts();
		model.addAttribute("list", list);
		
		return "ProductData";
	}
	
	
	//5. show Edit Page with data
	/**
	 * Read Request Param, make service call
	 * Get Object back, send to Edit Page if exist
	 * else redirect to data page
	 */
	@GetMapping("/edit")
	public String showEdit(
			@RequestParam Integer id,
			Model model
			)
	{
		String path = null;
		Optional<Product> opt = service.getOneProduct(id);
		if(opt.isEmpty()) { //not exist
			//redirect : response.sendRedirect(URL)
			path = "redirect:all";
		} else { //exist
			Product p = opt.get();
			//send to UI
			model.addAttribute("product", p);
			path = "ProductEdit";
		}
		
		return path;
	}
	
	
	//6. on click update modify in db
	/**
	 * Read Form data, call update service
	 * redirect to all page
	 */
	@PostMapping("/update")
	public String updateProduct(
			@ModelAttribute Product product 
			) 
	{
		service.updateProduct(product);
		return "redirect:all";
	}
	
}
