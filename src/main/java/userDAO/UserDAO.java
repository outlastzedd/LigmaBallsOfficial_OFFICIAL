package userDAO;

import jakarta.persistence.*;
import java.util.List;
import model.*;
import dao.*;
import productDAO.ProductDAO;

public class UserDAO implements IUserDAO
{
    private EntityManager em;
    
    public UserDAO()
    {
        em = DBConnection.getEntityManager();
    }
    
    public Users checkLogin(String email, String password)
    {
        try {
            return em.createNamedQuery("Users.normalLogin", Users.class).setParameter("email", email).setParameter("password", password).getSingleResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public Users checkLogin(String email)
    {
        try {
            return em.createNamedQuery("Users.GGLogin", Users.class).setParameter("email", email).getSingleResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public Users checkRegister(String fullname, String email, String phone, String password) {
        Users user = new Users(fullname, email, password, phone, "user", true);
        TypedQuery<Long> checkExistingQuery = em.createNamedQuery("Users.checkExisting", Long.class);
        checkExistingQuery.setParameter("email", email);
        checkExistingQuery.setParameter("phoneNumber", phone);
        if (checkExistingQuery.getSingleResult() == null || checkExistingQuery.getSingleResult() == 0) {
            insertUser(user);
            return user;
        } else
            return null;
    }

    @Override
    public void insertUser(Users user)
    {
        if (user == null)
            throw new IllegalArgumentException("User is null");
        em.getTransaction().begin();
        try
        {
            em.persist(user);
            em.getTransaction().commit();
        } catch (NoResultException e)
        {
            em.getTransaction().rollback();
        }
    }

    @Override
    public Users selectUser(int id) throws NoResultException
    {
        TypedQuery<Users> query = em.createNamedQuery("Users.selectByID", Users.class);
        query.setParameter("userID", id);
        return query.getSingleResult();
    }

    @Override
    public List<Users> selectAllUsers() throws NoResultException
    {
        TypedQuery<Users> query = em.createNamedQuery("Users.selectAll", Users.class);
        return query.getResultList();
    }

    @Override
    public void deleteUser(int id)
    {
        Integer userID = id;
        if (userID == null) 
            throw new IllegalArgumentException("UserID cannot be null");
        em.getTransaction().begin();
        try 
        {
            Users user = em.find(Users.class, userID);
            if (user == null) 
            {
                em.getTransaction().rollback();
                throw new RuntimeException("User not found with ID: " + userID);
            }
            em.remove(user);
            em.getTransaction().commit();
        } catch (Exception e) 
        {
            em.getTransaction().rollback();
            throw new RuntimeException("Failed to delete user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateUser(Users updatedUser) {
        if (updatedUser == null || updatedUser.getUserID() == null) {
            throw new IllegalArgumentException("User and UserID cannot be null");
        }

        em.getTransaction().begin();
        try {
            Users existingUser = em.find(Users.class, updatedUser.getUserID());
            if (existingUser == null) {
                em.getTransaction().rollback();
                return false; // User not found, return false instead of throwing exception
            }

            // Update fields
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setRole(updatedUser.getRole());

            em.getTransaction().commit();
            return true; // Update successful
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false; // Update failed due to an exception
        }
    }

    public boolean isEmailExists(String email) {
        String jpql = "SELECT COUNT(u) FROM Users u WHERE u.email = :email";
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

    public boolean isPhoneExists(String phone) {
        String jpql = "SELECT COUNT(u) FROM Users u WHERE u.phoneNumber = :phone";
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("phone", phone)
                .getSingleResult();
        return count > 0;
    }
}
