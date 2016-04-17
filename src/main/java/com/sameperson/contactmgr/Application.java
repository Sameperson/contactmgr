package com.sameperson.contactmgr;


import com.sameperson.contactmgr.model.Contact.ContactBuilder;
import com.sameperson.contactmgr.model.Contact;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;


public class Application {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("Dmitry", "Ponomarenko")
                .withEmail("mail@mail.com")
                .withPhone(123125254L)
                .build();
        int id = save(contact);

        System.out.printf("%n%nBefore update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        Contact c = findContactById(id);
        c.setFirstName("John");
        System.out.printf("%nUpdating...%n");
        update(c);
        System.out.printf("%nUpdate complete!%n");
        System.out.printf("%nAfter update%n");
        fetchAllContacts().stream().forEach(System.out::println);


    }

    private static Contact findContactById(int id) {
        Session session = sessionFactory.openSession();
        Contact contact = session.get(Contact.class, id);
        session.close();
        return contact;
    }

    private static void update(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(contact);
        session.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Contact.class);
        List<Contact> contacts = criteria.list();
        session.close();
        return contacts;
    }

    public static int save(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Integer id = (Integer)session.save(contact);
        session.getTransaction().commit();
        session.close();
        return id;
    }
}
