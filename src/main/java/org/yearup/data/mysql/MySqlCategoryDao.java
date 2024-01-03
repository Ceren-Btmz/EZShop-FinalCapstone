package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        // get all categories
        List<Category> categories = null;
        String query = "SELECT * FROM easyshop.categories;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            do {
                int categoryID = resultSet.getInt("category_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                categories.add(new Category(categoryID, name, description));
            } while (resultSet.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id
        Category category = new Category();
        String query = "SELECT * FROM easyshop.categories WHERE category_id = ?";
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, categoryId);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                category.setCategoryId(resultSet.getInt("category_id"));
                category.setName(resultSet.getString("name"));
                category.setDescription(resultSet.getString("description"));
                return category;
            }
        }
    } catch (SQLException e) {
            e.printStackTrace();
        }

        return category;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        String query = "INSERT INTO easyshop.categories (name, description) VALUES (?, ?)";

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Category creation failed, 0 rows were affected!");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedID = generatedKeys.getInt(1);
                    category.setCategoryId(generatedID);
                } else {
                    throw new SQLException("Category creation failed! No ID found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
        String query = "UPDATE easyshop.categories SET name = ? WHERE category_id = ?";

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category.getName());
            statement.setInt(2, categoryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId) {
        // delete category
        String query = "DELETE FROM Categories WHERE CategoryID = ?";

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, categoryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
